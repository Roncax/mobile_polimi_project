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
import java.util.*

private const val TAG = "NewQuestionViewModel"

class NewQuestionViewModel(private val application: Application) : ViewModel() {

    lateinit var categories: String
    var images: MutableList<Uri?> = mutableListOf()
    lateinit var coverImage: Uri
    lateinit var region: String
    lateinit var expiration: Date
    lateinit var sex: String
    lateinit var title: String
    var coverId = -1
    var maxUsers = -1

    fun onCreateNewQuestion() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val userlist: MutableList<String> = mutableListOf()

        val mDatabase = FirebaseDatabase.getInstance().reference
        val key = mDatabase.child("chats").push().key

        userlist.add(userId)
        val chatid = key!!

        mDatabase.child("users").child(userId).child("chatlist").child("your").child(key)
            .setValue(key)
        chooseChatUsers(chatid, userlist)
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
                kotlin.run loop@{
                    children.forEach {
                        if (userlist.size > maxUsers) {
                            return@loop
                        }
                        if (it.child("uid").value.toString() != userId) {
                            val validUser =
                                checkCountry(it.child("country").value.toString()) && checkGender(
                                    it.child("gender").value.toString()
                                ) && checkCategory(listOf(it.child("categories").value))

                            if (validUser) {
                                mDatabase.child("users").child(it.key!!).child("chatlist")
                                    .child("other")
                                    .child(chatId).setValue(chatId)

                                mDatabase.child("chats").child(chatId).child("userList")
                                    .child(it.key!!)
                                    .setValue(it.key!!)

                                userlist.add(it.key!!)
                            }
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


    private fun uploadImages(chatId: String) {

        coverId = (0..1000).random()
        val imagesRef: StorageReference? =
            FirebaseStorage.getInstance().reference.child("chat_images/${chatId}/${coverId}")

        imagesRef?.putFile(coverImage)

        for (image in images) {
            FirebaseStorage.getInstance().reference.child("chat_images/${chatId}/${(0..1000).random()}")
        }
    }


    private fun checkGender(gender: String): Boolean {
        return when (sex) {
            "Both" -> true
            else -> (gender == sex)
        }
    }

    private fun checkCountry(country: String): Boolean {
        return when (country) {
            "My country" -> (country == region)
            else -> true
        }
    }

    private fun checkCategory(categoryList: List<Any?>): Boolean {
        return categoryList.contains(categories)
    }
}

