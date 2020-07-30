package com.example.iadvice.chat

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iadvice.R
import com.example.iadvice.database.Message
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

private const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity() {

    //take the chatId from the previous screen (home in this case)
   /* val safeArgs: ChatActivityArgs by navArgs()
    val chatId = safeArgs.chatId
*/

    private lateinit var adapter: MessageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val intent = intent
        val chatId = intent.getStringExtra("chatId")

        val application = requireNotNull(this).application

        messageList.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this, chatId)
        messageList.adapter = adapter
        messageList.scrollToPosition(adapter.itemCount - 1)


        btnSend.setOnClickListener {
            if (txtMessage.text.isNotEmpty()) {
                val time = Calendar.getInstance().timeInMillis

                val message = Message(
                    chatId = chatId,
                    user = FirebaseAuth.getInstance().currentUser!!.uid,
                    nickname = this.getSharedPreferences(),
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


}
