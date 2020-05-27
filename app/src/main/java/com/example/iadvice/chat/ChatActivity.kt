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
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity() {

    //take the chatId from the previous screen (home in this case)
    //TODO mettere in safeargs il chatID dalla home
    //val safeArgs: ChatActivityArgs by navArgs()
    //val chatId = safeArgs.chatId
    val chatId = 123
    private lateinit var adapter: MessageAdapter

    private val pusherAppKey = "6e1f164ad49aa236076b"
    private val pusherAppCluster = "eu"

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

                Log.i(TAG, "The user ${App.user} sent the message ${txtMessage.text} at time $time")
                val call = ChatService.create().postMessage(message)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        resetInput()
                        if (!response.isSuccessful) {
                            Log.e(TAG, response.code().toString());
                            Toast.makeText(
                                applicationContext,
                                "Response was not successful",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        resetInput()
                        Log.e(TAG, t.toString());
                        Toast.makeText(
                            applicationContext,
                            "Error when calling the service",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            } else {
                Toast.makeText(
                    applicationContext,
                    "Message should not be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setupPusher()
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

    private fun setupPusher() {
        val options = PusherOptions()
        options.setCluster(pusherAppCluster)
        val pusher = Pusher(pusherAppKey, options)
        val channel = pusher.subscribe(chatId.toString())

        channel.bind("new_message") { channelId, eventName, data ->

            val jsonObject = JSONObject(data)
            val message = Message(
                jsonObject["chatId"].toString().toInt(),
                jsonObject["user"].toString(),
                jsonObject["text"].toString(),
                jsonObject["time"].toString().toLong()
            )

            runOnUiThread {
                adapter.addNewMessage(message)
                // scroll the RecyclerView to the last added element
                messageList.scrollToPosition(adapter.itemCount - 1)
            }

        }

        pusher.connect()
    }
}
