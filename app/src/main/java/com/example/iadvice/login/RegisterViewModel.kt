package com.example.iadvice.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.iadvice.database.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

private const val TAG = "LoginViewModel"

class RegisterViewModel : ViewModel() {

    lateinit var uid: String
    lateinit var username: String
    var age = -1
    lateinit var gender: String
    lateinit var uri: Uri
    lateinit var country: String
    lateinit var categories: MutableList<String>

    // register the user with the selected parameters
    fun registerUser() {
        val db = Firebase.database.reference

        val user = User(
            uid = uid,
            username = username,
            gender = gender,
            age = age,
            country = country
        )

        db.child("users").child(uid).setValue(user)

        for (category in categories) {
            db.child("users").child(uid).child("categories").child(category).setValue("true")
        }

        FirebaseStorage.getInstance().reference.child("avatar_images/$uid").putFile(uri)
    }


}