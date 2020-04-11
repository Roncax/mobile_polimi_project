package com.example.iadvice.login

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel

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

    fun registerUser(password: Editable, username: Editable) {
        Log.i(TAG, "Ricevuto registrzione di user $username con pw $password")
    }

    fun loginUser(password: Editable, username: Editable) {
        Log.i(TAG, "Ricevuto login di user $username con pw $password")
    }
}