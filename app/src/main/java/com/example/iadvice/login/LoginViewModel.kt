package com.example.iadvice.login

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.iadvice.database.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileInputStream


private const val TAG = "LoginViewModel"

class LoginViewModel(
    application: Application
) : ViewModel() {


    // register the user with the selected parameters
    fun registerUser(
        uid: String,
        username: String,
        age: Int,
        gender: String,
        uri: Uri
    ) {

        val categories = "ONE"
        val user = User(
            uid = uid,
            username = username,
            gender = gender,
            categories = categories,
            age = age
        )

        Firebase.database.reference.child("users").child(uid).setValue(user)

        // Create an instance of the storage
        val storage = FirebaseStorage.getInstance()

        // Create a storage reference from our app
        var storageRef = storage.reference

        // Create a child reference
        var imagesRef: StorageReference? = storageRef.child("avatar_images/"+uid)


        if (imagesRef != null) {
            imagesRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    // Uri: taskSnapshot.downloadUrl
                    // Name: taskSnapshot.metadata!!.name
                    // Path: taskSnapshot.metadata!!.path
                    // Size: taskSnapshot.metadata!!.sizeBytes
                }
                .addOnFailureListener { exception ->
                    // Handle unsuccessful uploads
                }
                .addOnProgressListener { taskSnapshot ->
                    // taskSnapshot.bytesTransferred
                    // taskSnapshot.totalByteCount
                }
                .addOnPausedListener { taskSnapshot ->
                    // Upload is paused
                }
        }
    }


}

