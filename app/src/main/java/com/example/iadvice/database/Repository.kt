package com.example.iadvice.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Repository {

    fun getUser(userId: String): LiveData<User> {
        val data = MutableLiveData<User>()

        //TODO prendere user da firebase


        return data
    }
}