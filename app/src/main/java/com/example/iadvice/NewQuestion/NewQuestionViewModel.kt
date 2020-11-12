package com.example.iadvice.newQuestion

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.iadvice.database.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private const val TAG = "NewQuestionViewModel"

class NewQuestionViewModel(private val application: Application) : ViewModel() {

    lateinit var categories: String
    var images: MutableList<Uri?> = mutableListOf()
    lateinit var region: String
    lateinit var expiration: String
    lateinit var sex: String
    lateinit var title: String
    var coverId = -1

    fun onCreateNewQuestion() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val userlist: MutableList<String> = mutableListOf()

        val mDatabase = FirebaseDatabase.getInstance().reference
        val key = mDatabase.child("chats").push().key

        userlist.add(userId)
        val chatid = key!!

        mDatabase.child("users").child(userId).child("chatlist").child("your").child(key).setValue(key)
        //calculateExpiration()
        chooseChatUsers(chatid, userlist)

    }

    private fun calculateExpiration() {
        TODO("Not yet implemented")
    }

    private fun chooseChatUsers(
        chatId: String,
        userlist: MutableList<String>
    ) {
        val mDatabase = FirebaseDatabase.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.children
                children.forEach {
                    if (it.child("uid").getValue().toString() != userId) {

                        if (it.child("gender").getValue().toString() == sex) {

                            mDatabase.child("users").child(it.key!!).child("chatlist")
                                .child("other")
                                .child(chatId).setValue(chatId)

                            mDatabase.child("chats").child(chatId).child("userList").child(it.key!!)
                                .setValue(it.key!!)

                            userlist.add(it.key!!)
                        }
                    }
                }
                uploadImages(chatId)
                val newChat = Chat(
                    chatId = chatId,
                    owner = userId,
                    question = title,
                    isActive = true,
                    userList = userlist,
                    expiration = expiration,
                    coverId = coverId
                )
                mDatabase.child("chats").child(chatId).setValue(newChat)


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i(TAG, "Error in choosing the chat users")
            }

        }
        mDatabase.child("users").addListenerForSingleValueEvent(userListener)
    }

    private fun uploadImages(chatId:String) {

        coverId = (0..1000).random()
        val imagesRef: StorageReference? = FirebaseStorage.getInstance().reference.child("chat_images/${chatId}/${coverId}")

        if (imagesRef != null && images.isNotEmpty()) {
            imagesRef.putFile(images[0]!!)
        }
    }
}

