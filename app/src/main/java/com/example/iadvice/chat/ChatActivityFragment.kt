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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.example.iadvice.database.Message
import com.example.iadvice.evaluation.CustomListViewEvaluationDialog
import com.example.iadvice.home.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat_fragment.*
import java.util.*
import com.example.iadvice.databinding.ActivityChatFragmentBinding


class ChatActivityFragment : Fragment() {

    companion object {
        fun newInstance() = ChatActivityFragment()
        const val TAG = "CHAT_ACTIVITY_FRAGMENT"
    }

    private lateinit var home_viewModel: HomeFragmentViewModel
    private lateinit var viewModel: ChatActivityViewModel
    private lateinit var adapter: MessageAdapter
    private lateinit var customDialog: CustomListViewEvaluationDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // bind the login_fragment layout with the binding variable
        val binding = DataBindingUtil.inflate<ActivityChatFragmentBinding>(
            inflater,
            R.layout.activity_chat_fragment, container, false
        )

        viewModel = ViewModelProvider(this).get(ChatActivityViewModel::class.java)
        home_viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)

        binding.messageList.layoutManager = LinearLayoutManager(context)
        adapter = MessageAdapter(requireContext(), viewModel.currentChatId)
        binding.messageList.adapter = adapter
        binding.messageList.scrollToPosition(adapter.itemCount - 1)

        loadMessages(viewModel.currentChatId, binding.messageList)


        binding.btnSend.setOnClickListener {
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
                Toast.makeText(context, "Message should not be empty", Toast.LENGTH_SHORT).show()
            }
        }


        val chatOwner = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (FirebaseAuth.getInstance().uid!! != dataSnapshot.value.toString()) {
                    binding.closeButton.visibility = View.GONE
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.i(TAG, "Error in choosing the chat users")
            }

        }
        FirebaseDatabase.getInstance().reference.child("chats").child(viewModel.currentChatId)
            .child("owner").addListenerForSingleValueEvent(chatOwner)



        binding.closeButton.setOnClickListener {
            clickHere()
        }

        return binding.root
    }


    fun clickHere() {
        val items = mutableMapOf<String, String>()

        val mDatabase = FirebaseDatabase.getInstance().reference

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.children
                children.forEach {
                    items[it.key.toString()] = it.value.toString()
                }

                customDialog = CustomListViewEvaluationDialog(
                    activity = requireActivity(),
                    usernameList = items
                )

                //if we know that the particular variable not null any time ,we can assign !! (not null operator ), then  it won't check for null, if it becomes null, it will throw exception
                customDialog.show()
                customDialog.setCanceledOnTouchOutside(false)
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.i(TAG, "Error in choosing the chat users")
            }

        }
        mDatabase.child("chats").child(viewModel.currentChatId).child("userList")
            .addListenerForSingleValueEvent(userListener)
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
                //TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue<Message>()
                adapter.addMessage(message!!)
                messageList.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                //TODO("Not yet implemented")
            }


        }

        onlineDb.child("messages").child(chatId!!).addChildEventListener(messagesListener)

    }

}