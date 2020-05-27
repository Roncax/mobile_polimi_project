package com.example.iadvice.login

import android.app.Application
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import com.example.iadvice.App
import com.example.iadvice.database.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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


    fun uploadUser(uid: String) {

            val userListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var retrivedUser = snapshot.getValue<User>()!!
                    App.user = retrivedUser
                    Log.d(TAG, "L'utente caricato é AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA: ${App.user.username}")
                }
            }

            Firebase.database.reference.child("users").child(uid)
                .addListenerForSingleValueEvent(userListener)


    }

}