package com.example.iadvice

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.iadvice.database.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import java.util.*



class ArchiveViewModel(private val application: Application) : ViewModel() {

    val TAG = "ArchiveViewModel"
    private lateinit var userId: String

    var archivedChatId: MutableList<String> = mutableListOf()
    var archivedChatList: MutableList<Chat> = mutableListOf()

    fun findChatsId() {

        Log.d(com.example.iadvice.home.TAG, "In onCreate")
        userId = FirebaseAuth.getInstance().uid!!


        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("chatlist")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.child("your").children) {
                        archivedChatId.add(snapshot.value.toString())
                    }
                    retrieveChatsFromId()
                }
            })
    }


    private fun retrieveChatsFromId() {
        val mDatabase = FirebaseDatabase.getInstance().reference
        FirebaseDatabase.getInstance().reference
            .child("chats")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    //TODO To implement
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (chatName in archivedChatId) {

                        val chat: Chat? = dataSnapshot.child(chatName).getValue(Chat::class.java)
                        if (chat?.isActive == false) {
                            archivedChatList.add(chat)
                        }
                    }
                }

            })
    }

}

