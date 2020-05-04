package com.example.iadvice.chat

import android.util.Log
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.App
import com.example.iadvice.DateUtils
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.database.ChatDao
import com.example.iadvice.database.Message
import com.example.iadvice.database.User
import kotlinx.android.synthetic.main.my_bubble.view.*
import kotlinx.android.synthetic.main.other_bubble.view.*

//possono essere messi in un companion object
private const val VIEW_TYPE_MY_MESSAGE = 1
private const val VIEW_TYPE_OTHER_MESSAGE = 2

private const val TAG = "MessageAdapter"

class MessageAdapter (val context: Context, val chatDataSource: ChatDao, Id: Int) : RecyclerView.Adapter<MessageViewHolder>() {

    private val messages: ArrayList<Message> = ArrayList()
    val chatId = Id

    init {
        val sampleFirstName = "Paolo"
        val sampleEmail = "paolo.roncaglioni@gmail.com"
        val sampleChatId = 123
        val sampleTxt1 = "Questo é un messaggio"
        val sampleTxt2 = "Immagine"
        val user = User(sampleFirstName, sampleEmail, 25, "male", "", "Roncax", "eltinto1")
        val chat = Chat(sampleChatId, sampleFirstName, "chat_di_prova")
        val sampleMessage = Message(sampleChatId, sampleFirstName, sampleTxt1, 1234)
        val sampleMessage2 = Message(sampleChatId, sampleFirstName, sampleTxt2, 124)

//        chatDataSource.insert(chat)
//        chatDataSource.insert(sampleMessage)
//        chatDataSource.insert(sampleMessage2)
        loadMessages()
    }

    fun loadMessages(){
        val oldMessages = chatDataSource.getChatWithMessagesFromId(chatId)
        for (message in oldMessages.messages) {
            messages.add(message)
        }
    }

    fun addMessage(message: Message){
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages.get(position)

        return if(App.user == message.username) {
            VIEW_TYPE_MY_MESSAGE
        }
        else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.my_bubble, parent, false))
        } else {
            OtherMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.other_bubble, parent, false))
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages.get(position)

        holder?.bind(message)
    }

    inner class MyMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtMyMessage
        private var timeText: TextView = view.txtMyMessageTime

        override fun bind(message: Message) {
            messageText.text = message.text
            timeText.text =
                DateUtils.fromMillisToTimeString(message.time!!.toLong())
            Log.i(TAG, "Sono dentro al mio messaggio, ${message.text}")
        }
    }

    inner class OtherMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtOtherMessage
        private var userText: TextView = view.txtOtherUser
    private var timeText: TextView = view.txtOtherMessageTime

        override fun bind(message: Message) {
            messageText.text = message.text
            userText.text = message.username
            timeText.text =
                DateUtils.fromMillisToTimeString(message.time!!.toLong())

            }
    }
}

open class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: Message) {}
}