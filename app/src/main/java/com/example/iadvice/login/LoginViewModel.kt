package com.example.iadvice.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.iadvice.App
import com.example.iadvice.R
import com.example.iadvice.database.User
import com.example.iadvice.database.UserDao

private const val TAG = "LoginViewModel"

class LoginViewModel(
    val database: UserDao,
    application: Application
) : ViewModel() {


    override fun onCleared() {
        super.onCleared()
        Log.i("LoginViewModel", "LoginViewModel destroyed!")
    }


    // register the user with the selected parameters
    fun registerUser(
        name: String,
        nickname: String,
        email: String,
        password: String,
        personalPhoto: String,
        age: Int,
        gender: String
    ) {
        val user = User(name, email, age, gender, personalPhoto, nickname, password)
        database.insert(user)
        Log.i(TAG, "Ricevuto e inserito l'utente $nickname con pw $password")
    }


    // TODO transfer the check of pw online
    fun loginUser(password: String, email: String) {
        Log.i(TAG, "Ricevuto login di user $email con pw $password")
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val retrivedUser = database.findByEmail(email)
            Log.i(TAG,"Login riuscito! name: ${retrivedUser.firstName} e genre: ${retrivedUser.gender}")
            App.user = email
        } else {
            Log.i(TAG, "Empty username or password")
        }
    }

}