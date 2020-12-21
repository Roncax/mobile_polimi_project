package com.example.iadvice

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.iadvice.database.User
import com.example.iadvice.login.LoginFragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference


object PersistenceUtils {

    lateinit var currenChatId: String


    var userListRank:MutableList<User> = mutableListOf()
    val userListRankLiveData: MutableLiveData<User> by lazy {
        MutableLiveData<User>(User())
    }

    var currentUser: User = User()
    val currentUserLiveData: MutableLiveData<User> by lazy {
        MutableLiveData<User>(User())
    }

    lateinit var currentUserImage: StorageReference
    val currentUserImageLiveData: MutableLiveData<StorageReference> by lazy {
        MutableLiveData<StorageReference>()
    }

    lateinit var allUserImages: MutableMap<String, StorageReference>
    val allUserImagesLiveData: MutableLiveData<MutableMap<String, StorageReference>> by lazy {
        MutableLiveData<MutableMap<String, StorageReference>>()
    }

    var currentChatImages: MutableList<StorageReference> = mutableListOf()
    val currentChatImagesLiveData: MutableLiveData<MutableList<StorageReference>> by lazy {
        MutableLiveData<MutableList<StorageReference>>()
    }



    fun updateCurrentUser(user: User){
        currentUser = user
        currentUserLiveData.value = user
    }

    fun updatecurrentUserImage(storageReference: StorageReference){
        currentUserImage = storageReference
        currentUserImageLiveData.value = storageReference
    }

    fun updateAllUserImages(images: MutableMap<String, StorageReference>){
        allUserImages = images
        allUserImagesLiveData.value = images
    }

    fun updateChatImages(images: MutableList<StorageReference>){
        currentChatImages = images
        currentChatImagesLiveData.value = images
    }

    const val TAG = "PERSISTENCE_UTILS"


    init {
        if (!FirebaseAuth.getInstance().currentUser?.uid.isNullOrBlank()) {
            retrieveUser()
        }
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
                    userListRank.sortBy { it.points }
                    userListRank.reverse()
                }
                retrieveUserImages()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }
        onlineDb.child("users").addListenerForSingleValueEvent(userListener)
    }

    private fun retrieveUserImages(){
        allUserImages = mutableMapOf()
        userListRank.forEach{
            val userId = it.uid
            val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("avatar_images/" + userId)
            allUserImages[userId] = imageRef
        }
        updateAllUserImages(allUserImages)
    }


    fun retrieveChatImages(){


        val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("chat_images/" + currenChatId)

        val listAllTask: Task<ListResult> = imageRef.listAll()
        listAllTask.addOnCompleteListener { result ->
            // should check for errors here first
            val tempList = mutableListOf<StorageReference>()
            val items: List<StorageReference> = result.result!!.items
            for (item in items) {
                Log.d(TAG, "Item retrieved $item")
                tempList.add(item)
            }
            updateChatImages(tempList)
        }




    }

}


