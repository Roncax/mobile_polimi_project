package com.example.iadvice.chat

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R


// used for the message to include many different content
abstract class MessageViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}

// holder for my messages (uses findbyView, not data binding)
class MyMessageViewHolder(val view: View) : MessageViewHolder<MessageItemUi>(view) {
    private val messageContent = view.findViewById<TextView>(R.id.message)

    override fun bind(item: MessageItemUi) {
        messageContent.text = item.content
        //messageContent.textColor = item.textColor
    }


}

// holder for the messages for the other people
class FriendMessageViewHolder(val view: View) : MessageViewHolder<MessageItemUi>(view) {
    private val messageContent = view.findViewById<TextView>(R.id.message)

    override fun bind(item: MessageItemUi) {
        messageContent.text = item.content
        //messageContent.textColor = item.textColor
    }


}