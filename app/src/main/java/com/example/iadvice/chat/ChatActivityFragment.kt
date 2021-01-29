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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.util.*


class ChatActivityFragment : Fragment() {

    companion object {
        const val TAG = "CHAT_ACTIVITY_FRAGMENT"
    }

    private lateinit var viewModel: ChatActivityViewModel
    private lateinit var adapter: MessageAdapter
    private lateinit var customDialog: CustomListViewEvaluationDialog
    private lateinit var binding: ActivityChatFragmentBinding

    private val chatObserver = Observer<Chat> { chat ->
        //TODO rimettere closebutton
        """if (FirebaseAuth.getInstance().currentUser!!.uid == viewModel.currentChat.owner.keys.first()) {
            binding.closeButton.visibility = View.VISIBLE
        }"""

        Log.d(TAG,
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

            showInfochatButton.setOnClickListener {
                findNavController().navigate(R.id.action_chatActivityFragment_to_chatInformations)
            }

            //TODO rimettere closedbutton
           """ closeButton.setOnClickListener {
                Log.d(TAG, "BuildEvaluationDialog")
                buildEvaluationDialog()
            }"""
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