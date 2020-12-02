package com.example.iadvice

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.iadvice.database.User
import com.example.iadvice.login.LoginFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object PersistenceUtils {

    lateinit var currenChatId: String
    lateinit var userListRank:MutableList<User>

    var currentUser: User = User()
    val currentUserLiveData: MutableLiveData<User> by lazy {
        MutableLiveData<User>(User())
    }

    lateinit var currentUserImage: StorageReference
    val currentUserImageLiveData: MutableLiveData<StorageReference> by lazy {
        MutableLiveData<StorageReference>()
    }

    fun updateCurrentUser(user: User){
        currentUser = user
        currentUserLiveData.value = user
    }

    fun updatecurrentUserImage(storageReference: StorageReference){
        currentUserImage = storageReference
        currentUserImageLiveData.value = storageReference
    }

    const val TAG = "PERSISTENCE_UTILS"


    init {
        retrieveUser()
        retrieveSortedUserList()
    }


    fun retrieveUser() {

        if(FirebaseAuth.getInstance().uid.isNullOrBlank())
        {
         return
        }
        val userId = FirebaseAuth.getInstance().uid!!


        val messagesUploadListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                //method that is called if the read is canceled (eg no permission)
                Log.w(LoginFragment.TAG, "loadPost:onCancelled", databaseError.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user: User? = p0.getValue(User::class.java)
                updateCurrentUser(user = user!!)
            }
        }

        Firebase.database.reference.child("users").child(userId).addValueEventListener(messagesUploadListener)
    }


    fun retrieveCurrentUserImage(){
        val userId = FirebaseAuth.getInstance().uid!!
        val imageRef: StorageReference? = FirebaseStorage.getInstance().reference.child("avatar_images/" + userId)
        updatecurrentUserImage(imageRef!!)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addEvaluationToUser(usernameListEvaluation: MutableMap<String, String>) {

        usernameListEvaluation.forEach { k, v ->

            val evaluationUploader = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    //method that is called if the read is canceled (eg no permission)
                    Log.w(LoginFragment.TAG, "loadEval:onCancelled", databaseError.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val oldValue = p0.value.toString().toInt()
                    val newValue = oldValue+v.toInt()
                    Firebase.database.reference.child("users").child(k).child("points").setValue(newValue)

                }
            }

            Firebase.database.reference.child("users").child(k).child("points").addListenerForSingleValueEvent(evaluationUploader)


        }

    }

    private fun retrieveSortedUserList() {
        userListRank = mutableListOf()

        val onlineDb = Firebase.database.reference
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{u ->
                    val user: User? = u.getValue(User::class.java)
                    userListRank.add(user!!)
                    Log.d(TAG, "User ${user.username} download")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }
        onlineDb.child("users").addListenerForSingleValueEvent(userListener)
    }

}
