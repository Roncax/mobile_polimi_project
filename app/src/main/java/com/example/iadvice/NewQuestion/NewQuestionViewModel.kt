package com.example.iadvice.newQuestion

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.database.Chat
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

private const val TAG = "NEWQUESTION_VIEWMODEL"

class NewQuestionViewModel(private val application: Application) : ViewModel() {

    lateinit var category: String
    lateinit var question: String
    var images: MutableList<Uri> = mutableListOf()
    var coverImage: Uri = Uri.EMPTY
    lateinit var region: String
    lateinit var expiration: Date
    lateinit var sex: String
    lateinit var title: String
    var coverId = -1
    var maxUsers = 5
    lateinit var realCountry: String
    lateinit var realSex: String

    val newChatLiveData: MutableLiveData<Chat> by lazy {
        MutableLiveData<Chat>()
    }

    fun onCreateNewQuestion() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid


        Log.d(TAG, "CreateNewQuestion")
        val mDatabase = FirebaseDatabase.getInstance().reference
        val key = mDatabase.child("chats").push().key


        mDatabase.child("users").child(userId).child("chatlist").child("your").child(key!!)
            .setValue(key)
        chooseChatUsers(key)
    }

    private fun chooseChatUsers(
        chatId: String
    ) {
        val mDatabase = FirebaseDatabase.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val userMap = mutableMapOf<String, String>()


        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.children
                realCountry = dataSnapshot.child(userId).child("country").value.toString()
                realSex = dataSnapshot.child(userId).child("gender").value.toString()

                Log.d(TAG, "real Country: $realCountry")
                Log.d(TAG, "real Category: $category")
                Log.d(TAG, "real Gender: $realSex")

                kotlin.run loop@{
                    Log.d(TAG, "In loop")

                    children.forEach {

                        if (userMap.size > maxUsers) {
                            return@loop
                        }
                        if (it.child("uid").value.toString() != userId) {
                            val country = it.child("country").value.toString()
                            val gender = it.child("gender").value.toString()
                            var categories = mutableListOf<String>()

                            it.child("categories").children.forEach {
                                categories.add(it.key.toString())
                            }

                            val validUser =
                                checkCountry(country = country) && checkCategory(categories) && checkGender(gender)

                            Log.d(TAG, "Country: $country")
                            Log.d(TAG, "Category: $categories")
                            Log.d(TAG, "Gender: $gender")
                            Log.d(TAG, "Is valid?: $validUser")

                            if (validUser) {
                                Log.d(TAG, "On true validUser id: ${it.key}")
                                userMap[it.key!!] = it.child("username").value.toString()
                            }
                        }
                    }
                }


                uploadImages(chatId)
                val newChat = Chat(
                    chatId = chatId,
                    owner = mutableMapOf(userId to PersistenceUtils.currentUser.username),
                    title = title,
                    isActive = true,
                    userList = userMap,
                    expiration = expiration,
                    coverId = coverId,
                    question = question,
                    category = category
                )
                mDatabase.child("chats").child(chatId).setValue(newChat)

                    for((k, v) in userMap){
                        mDatabase.child("users").child(k).child("chatlist")
                            .child("other")
                            .child(chatId).setValue(chatId)

                        mDatabase.child("chats").child(chatId).child("userList")
                            .child(k)
                            .setValue(v)
                    }

                Log.d(TAG, "The chat $chatId is uploaded")
                newChatLiveData.value = newChat

            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.i(TAG, "Error in choosing the chat users")
            }

        }
        mDatabase.child("users").addListenerForSingleValueEvent(userListener)
    }


    private fun uploadImages(chatId: String) {

        coverId = (0..1000).random()
        val imagesRef: StorageReference =
            FirebaseStorage.getInstance().reference.child("chat_images/${chatId}/${coverId}")

        imagesRef.putFile(coverImage)

        for (image in images) {
            Log.d(TAG, "Image $image put on firebase")
            val imageId = (0..1000).random()
            val imagesRef: StorageReference =  FirebaseStorage.getInstance().reference.child("chat_images/${chatId}/$imageId")
            imagesRef.putFile(image)
        }
    }


    private fun checkGender(gender: String): Boolean {
        return when (sex) {
            "Both" -> true
            else -> (gender == realSex)
        }
    }

    private fun checkCountry(country: String): Boolean {
        return when (region) {
            "My country" -> (country == realCountry)
            else -> true
        }
    }

    private fun checkCategory(categoryList: List<Any?>): Boolean {
        return categoryList.contains(category)
    }
}

