package com.example.iadvice.chat

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.database.Chat
import com.example.iadvice.database.Message
import com.example.iadvice.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ChatActivityViewModel : ViewModel() {

    var currentChatId: String = PersistenceUtils.currenChatId
    lateinit var currentChat: Chat
     val currentChatLiveData: MutableLiveData<Chat> by lazy {
         MutableLiveData<Chat>()
    }

    var messageList = mutableListOf<Message>()

init {
    retrieveChat()
}

    fun retrieveChat(){
        val chatOwner = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chat = dataSnapshot.getValue<Chat>()
                currentChat = chat!!
                PersistenceUtils.currentChatCoverId = currentChat.coverId.toString()
                currentChatLiveData.value = chat
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i(ChatActivityFragment.TAG, "Error in choosing the chat users")
            }

        }
        FirebaseDatabase.getInstance().reference.child("chats").child(currentChatId).addValueEventListener(chatOwner)
    }


}