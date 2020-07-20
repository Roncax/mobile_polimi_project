package com.example.iadvice.login

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.iadvice.database.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "LoginViewModel"

class LoginViewModel(
    application: Application
) : ViewModel() {


    // register the user with the selected parameters
    fun registerUser(
        uid: String,
        username: String,
        age: Int,
        gender: String
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

    }

}