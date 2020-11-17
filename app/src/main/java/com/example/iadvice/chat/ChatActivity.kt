package com.example.iadvice.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.database.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity() {
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val intent = intent
        val chatId = intent.getStringExtra("chatId")

        messageList.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this, chatId)
        messageList.adapter = adapter
        messageList.scrollToPosition(adapter.itemCount - 1)

        loadMessages(chatId, messageList)


        btnSend.setOnClickListener {
            if (txtMessage.text.isNotEmpty()) {
                val time = Calendar.getInstance().timeInMillis
                val sharedPreference =  getSharedPreferences("USERS",Context.MODE_PRIVATE)
                val user_nick = sharedPreference.getString("username","defaultName")
                val message = Message(
                    chatId = chatId,
                    user = FirebaseAuth.getInstance().currentUser!!.uid,
                    nickname = user_nick!!,
                    text = txtMessage.text.toString(),
                    time = time
                )
                adapter.addNewMessage(message)

                // scroll the RecyclerView to the last added element
                messageList.scrollToPosition(adapter.itemCount - 1)
                resetInput()

            } else {
                Toast.makeText(applicationContext, "Message should not be empty", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun resetInput() {
        // Clean text box
        txtMessage.text.clear()

        // Hide keyboard
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
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
