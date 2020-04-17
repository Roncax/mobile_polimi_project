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

    //login screen button
    fun registerUserButton(password: Editable, username: Editable) {
        Log.i(TAG, "Ricevuto registrazione di user $username con pw $password")
    }

    // register screen button
    fun registerUser(name: String, nickname: String, email: String,  password: String, personalPhoto: String){
        Log.i(TAG, "Ricevuta registrazione di user $nickname con pw $password")
    }

    fun loginUserButton(password: Editable, username: Editable) {
        Log.i(TAG, "Ricevuto login di user $username con pw $password")
    }

}