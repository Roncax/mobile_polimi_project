package com.example.iadvice.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.iadvice.DateUtils
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.database.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.my_bubble.view.*
import kotlinx.android.synthetic.main.other_bubble.view.*


class MessageAdapter(val context: Context, Id: String) : RecyclerView.Adapter<MessageViewHolder>() {

    private val messages: ArrayList<Message> = ArrayList()
    val chatId = Id

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2
        private const val TAG = "MessageAdapter"
    }


    init {
        loadMessages()
    }

    fun loadMessages() {
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
                addMessage(message!!)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                //TODO("Not yet implemented")
            }


        }

        onlineDb.child("messages").child(chatId).addChildEventListener(messagesListener)

    }

    fun addNewMessage(message: Message) {
        var onlineDb = Firebase.database.reference
        val key = onlineDb.child("messages").child(chatId).push().key
        onlineDb.child("messages").child(message.chatId).child(key!!).setValue(message)
    }


    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun getItemCount() = messages.size


    override fun getItemViewType(position: Int): Int {
        val message = messages.get(position)
        return if (FirebaseAuth.getInstance().currentUser!!.uid == message.user) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.my_bubble, parent, false)
            )
        } else {
            OtherMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.other_bubble, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages.get(position)
        holder.bind(message)
    }

    inner class MyMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtMyMessage
        private var timeText: TextView = view.txtMyMessageTime

        override fun bind(message: Message) {
            messageText.text = message.text
            timeText.text =
                DateUtils.fromMillisToTimeString(message.time)
        }
    }

    inner class OtherMessageViewHolder(view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.txtOtherMessage
        private var userText: TextView = view.txtOtherUser
        private var timeText: TextView = view.txtOtherMessageTime
        private var userImage: ImageView = view.other_image_chat


        override fun bind(message: Message) {
            messageText.text = message.text
            userText.text = message.nickname
            timeText.text =
                DateUtils.fromMillisToTimeString(message.time)


            // Create an instance of the storage
            val storage = FirebaseStorage.getInstance()

            // Create a storage reference from our app
            val storageRef = storage.reference

            // Create a child reference
            val imageRef: StorageReference? = storageRef.child("avatar_images/" + message.user)


            GlideApp.with(context)
                .load(imageRef)
                .fitCenter()
                .circleCrop()
                .into(userImage)
        }
    }
}

open class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: Message) {}
}