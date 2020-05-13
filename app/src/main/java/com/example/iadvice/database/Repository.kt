package com.example.iadvice.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database: AppDatabase) {

    private lateinit var onlineDb: DatabaseReference

    fun getUserFromRepository(email: String): LiveData<User> {
        val data = MutableLiveData<User>()

        return data
    }

    /**
     * Refresh chat cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshChats() {
        onlineDb = Firebase.database.reference

        withContext(Dispatchers.IO) {

        }
    }
}