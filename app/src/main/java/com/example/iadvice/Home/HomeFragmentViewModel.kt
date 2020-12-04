package com.example.iadvice.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iadvice.database.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class HomeFragmentViewModel : ViewModel() {

    companion object {
        const val TAG = "HOME_FRAGMENT_VIEWMODEL"
    }

    init {
        fetchList()
    }

    lateinit var userId: String

    var myChatList: MutableList<Chat> = mutableListOf()
    val myChatListLiveData: MutableLiveData<MutableList<Chat>> by lazy {
        MutableLiveData<MutableList<Chat>>(mutableListOf())
    }

    var otherChatList: MutableList<Chat> = mutableListOf()
    val otherChatListLiveData: MutableLiveData<MutableList<Chat>> by lazy {
        MutableLiveData<MutableList<Chat>>(mutableListOf())
    }


    var archivedChatList: MutableList<Chat> = mutableListOf()
    val archivedChatListLiveData: MutableLiveData<MutableList<Chat>> by lazy {
        MutableLiveData<MutableList<Chat>>(mutableListOf())
    }


    fun fetchList() {
        findChatsId(type = "your")
        findChatsId(type = "other")
    }

    private fun findChatsId(type: String) {
        userId = FirebaseAuth.getInstance().uid!!

        val chatListDB = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("chatlist")
            .child(type)

        chatListDB.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.e(TAG, "Cancelled ids retrieve on fetch '$type' data")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chatIdList = mutableListOf<String>()
                for (snapshot in dataSnapshot.children) {
                    chatIdList.add(snapshot.value.toString())
                    Log.d(TAG, "Chat '${snapshot.value.toString()}' in type '$type' added")
                }
                retrieveChatsFromId(chatIdList, type)
            }
        })
    }


    private fun retrieveChatsFromId(
        chatIdList: MutableList<String>,
        type: String
    ) {
        val temporaryList = mutableListOf<Chat>()

        val chatListDB = FirebaseDatabase.getInstance().reference
            .child("chats")

        chatListDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.e(TAG, "Cancelled chats retrieve of '$type' data")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (chatName in chatIdList) {
                    Log.d(TAG, "Chat name '$chatName' in type '$type' is in")
                    val chat: Chat? = dataSnapshot.child(chatName).getValue(Chat::class.java)
                    if (chat != null) {
                        if (chat.expiration.before(Calendar.getInstance().time)) {
                            chatListDB.child(chatName).child("active").setValue(false)
                        }

                        Log.d(TAG, "Chat object '$chat' in type '$type' added")
                        if (chat.isActive) {
                            Log.d(TAG, "Chat '$chatName' is active")
                            when (type) {
                                "your" -> chat.let { temporaryList.add(it) }
                                "other" -> chat.let { temporaryList.add(it) }
                                else -> Log.e(TAG, "Type not supported")
                            }
                        } else {
                            Log.d(TAG, "Chat '$chatName' is not active")
                            if (!archivedChatList.contains(chat)) {
                                chat.let { archivedChatList.add(it) }
                                archivedChatListLiveData.setValue(archivedChatList)
                            }

                            Log.d(TAG, "ARCHIVIATE ${archivedChatList}")
                        }
                    }
                }
                when (type) {
                    "your" -> {
                        myChatList = temporaryList
                        myChatListLiveData.setValue(myChatList)
                    }
                    "other" -> {
                        otherChatList = temporaryList
                        otherChatListLiveData.setValue(otherChatList)
                    }
                    else -> Log.e(TAG, "Type not supported")
                }

            }
        })
    }
}