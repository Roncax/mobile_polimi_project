package com.example.iadvice.chat

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.iadvice.GlideApp
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.database.Message
import com.example.iadvice.evaluation.CustomListViewEvaluationDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat_fragment.*
import com.example.iadvice.databinding.ActivityChatFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_chat.*
import kotlinx.android.synthetic.main.toolbar_chat.view.*
import java.util.*
import kotlin.properties.Delegates


class ChatActivityFragment : Fragment() {

    companion object {
        const val TAG = "CHAT_ACTIVITY_FRAGMENT"
    }

    private lateinit var viewModel: ChatActivityViewModel
    private lateinit var adapter: MessageAdapter
    private lateinit var customDialog: CustomListViewEvaluationDialog
    private lateinit var binding: ActivityChatFragmentBinding

    private var isTablet by Delegates.notNull<Boolean>()

    private val chatObserver = Observer<Chat> { chat ->
        binding.toolbarChat.chat_title.text = viewModel.currentChat.title

        if (!chat.isActive) {
            binding.materialCardView.visibility = View.GONE
        }

        //Caricamento dell'immagine cover della chat
        val imageRef: StorageReference =
            FirebaseStorage.getInstance().reference.child("chat_images/${viewModel.currentChat.chatId}/${viewModel.currentChat.coverId}")
        GlideApp.with(requireView())
            .load(imageRef)
            .circleCrop()
            .into(binding.toolbarChat.image_toolbar)


        if (FirebaseAuth.getInstance().currentUser!!.uid == viewModel.currentChat.owner.keys.first()) {
            binding.toolbarChat.menu.clear()
            binding.toolbarChat.inflateMenu(R.menu.chat_menu)
        }

        Log.d(
            TAG,
            "Visible close button '${chat}' owner:${FirebaseAuth.getInstance().currentUser!!.uid} me: ${viewModel.currentChat.owner.keys.first()}"
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.activity_chat_fragment, container, false
        )

        viewModel = ViewModelProvider(this).get(ChatActivityViewModel::class.java)
        adapter = MessageAdapter(requireContext(), viewModel)

        loadMessages(viewModel.currentChatId, binding.messageList)
        viewModel.currentChatLiveData.observe(viewLifecycleOwner, chatObserver)

        isTablet = context?.resources?.getBoolean(R.bool.isTablet)!!

        //if tablet version do not show arrow for back navigation
        if(isTablet)
            binding.toolbarChat.navigationIcon = null



        //nascondo l'altra appbar
        if (!isTablet)
            requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.GONE

        binding.apply {
            messageList.layoutManager = LinearLayoutManager(context)
            messageList.adapter = adapter
            messageList.scrollToPosition(adapter.itemCount - 1)
            btnSend.setOnClickListener {
                if (txtMessage.text.isNotEmpty()) {
                    val message = Message(
                        chatId = viewModel.currentChatId,
                        user = FirebaseAuth.getInstance().currentUser!!.uid,
                        nickname = PersistenceUtils.currentUser.username,
                        text = txtMessage.text.toString(),
                        time = Calendar.getInstance().timeInMillis
                    )
                    adapter.addNewMessage(message)

                    // scroll the RecyclerView to the last added element
                    binding.messageList.scrollToPosition(adapter.itemCount - 1)
                    resetInput()

                } else {
                    Toast.makeText(context, "Message should not be empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }


        binding.toolbarChat.setNavigationOnClickListener {
            popStack()
        }
        binding.toolbarChat.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.chatInformationsFragment -> {
                    if (!isTablet) {
                        findNavController().navigate(R.id.action_chatActivityFragment_to_chatInformations)
                        Log.d(TAG, "Clicked chat info")
                    } else {
                        findNavController().navigate(R.id.action_chatActivityFragment2_to_chatInformationsFragment2)
                    }
                }
                R.id.close_chat_item -> {
                    Log.d(TAG, "BuildEvaluationDialog")
                    buildEvaluationDialog()
                }
            }


            return@setOnMenuItemClickListener true
        }

        return binding.root
    }


    private fun buildEvaluationDialog() {
        Log.d(TAG, "BuildEvaluationDialog")
        val items = mutableMapOf<String, String>()
        viewModel.currentChat.userList.forEach {
            items[it.key] = it.value
        }

        customDialog = CustomListViewEvaluationDialog(
            activity = requireActivity(),
            usernameList = items
        )

        customDialog.show()
        customDialog.setCanceledOnTouchOutside(false)

    }

    fun popStack() {
        if (requireView().findNavController().popBackStack()) {
            requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout).visibility =
                View.VISIBLE
        }
    }

    private fun resetInput() {
        // Clean text box
        txtMessage.text.clear()
        val view = this.view
        if (view != null) {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

        }
    }


    private fun loadMessages(chatId: String?, messageList: RecyclerView) {
        val onlineDb = Firebase.database.reference
        val messagesListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", p0.toException())
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue<Message>()
                adapter.addMessage(message!!)
                messageList.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }

        onlineDb.child("messages").child(chatId!!).addChildEventListener(messagesListener)

    }

}