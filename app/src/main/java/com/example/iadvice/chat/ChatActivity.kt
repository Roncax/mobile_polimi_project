package com.example.iadvice.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iadvice.*
import com.example.iadvice.database.Message
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*


private const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity() {

    //take the chatId from the previous screen (home in this case)
    //TODO mettere in safeargs il chatID dalla home
    //val safeArgs: ChatActivityArgs by navArgs()
    //val chatId = safeArgs.chatId
    val chatId = 123
    private lateinit var adapter: MessageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

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
                    user = App.user,
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
