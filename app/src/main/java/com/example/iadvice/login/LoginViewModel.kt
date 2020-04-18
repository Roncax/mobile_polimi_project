package com.example.iadvice.login

import android.content.Intent
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.example.iadvice.App
import com.example.iadvice.chat.ChatActivity
import com.example.iadvice.database.dbManager
import kotlinx.android.synthetic.main.activity_main.*

// always use AppCompatActivity for backward compatibility
class LoginViewModel : ViewModel() {

    init {
        Log.i("LoginViewModel", "LoginViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("LoginViewModel", "LoginViewModel destroyed!")
    }

    private val TAG = "LoginViewModel"

    //login screen button
    fun registerUserButton(password: Editable, username: Editable) {
        Log.i(TAG, "Ricevuto registrazione di user $username con pw $password")
    }

    // register screen button
    fun registerUser(
        name: String,
        nickname: String,
        email: String,
        password: String,
        personalPhoto: String
    ) {
        Log.i(TAG, "Ricevuta registrazione di user $nickname con pw $password")
    }

    fun loginUserButton(password: String, username: String) {
        Log.i(TAG, "Ricevuto login di user $username con pw $password")
        if (username.isNotEmpty()) {
            App.user = username
            Log.i(TAG, "Accepted login")
            if (password.isNotEmpty()) {

            } else {
                Log.i(TAG, "Empty password")
            }
        } else {
            Log.i(TAG, "Empty username")
        }
    }

}