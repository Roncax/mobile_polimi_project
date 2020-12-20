package com.example.iadvice.settings

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.database.Chat
import com.example.iadvice.database.User
import com.example.iadvice.home.HomeFragment
import com.example.iadvice.home.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

private const val TAG = "SettingsViewModel"

class SettingsViewModel : ViewModel() {

    lateinit var userId: String
    lateinit var uri: Uri
    //lateinit var categories: MutableList<String>

    var categoriesList: MutableMap<String, String> = mutableMapOf<String, String>()
    val categoriesListLiveData: MutableLiveData<MutableMap<String, String>> by lazy {
        MutableLiveData<MutableMap<String, String>>()
    }


    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String>
        get() = _gender

    private val _country = MutableLiveData<String>()
    val country: LiveData<String>
        get() = _country

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int>
        get() = _age


    init {
        _age.value = -1
    }

    fun setAge(age: Int) {
        _age.value = age
        updateUser()
    }

    fun setUsername(username: String) {
        _username.value = username
        updateUser()
    }

    fun setGender(gender: String) {
        _gender.value = gender
        updateUser()
    }

    fun setCountry(country: String) {
        _country.value = country
        updateUser()
    }

    // update user with the selected parameters
    fun updateUser() {
        val db = Firebase.database.reference

        val user = _age.value?.let {
            User(
                uid = userId,
                username = _username.value.toString(),
                gender = _gender.value.toString(),
                age = it.toInt(),
                country = _country.value.toString()
            )
        }

        db.child("users").child(userId).setValue(user)


        for (category in categoriesList.keys) {
            db.child("users").child(userId).child("categories").child(category).setValue("true")
        }

        /*
       FirebaseStorage.getInstance().reference.child("avatar_images/$userId").putFile(uri)
        */

    }


    fun getUser() {
        userId = PersistenceUtils.currentUser.uid
        _gender.value = PersistenceUtils.currentUser.gender
        _username.value = PersistenceUtils.currentUser.username
        _age.value = PersistenceUtils.currentUser.age
        _country.value = PersistenceUtils.currentUser.country
    }

/*
    fun getUserCategories() {

        userId = FirebaseAuth.getInstance().uid!!

        val userListener = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("categories")


        userListener.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.e(TAG, "Cancelled ids retrieve on fetch user '$userId'")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    categoriesList.add(snapshot.value.toString())
                    Log.d(
                        HomeFragmentViewModel.TAG,
                        "category '${snapshot.value.toString()}' added to user '${userId}'"
                    )
                }
                categoriesListLiveData.setValue(categoriesList)
            }
        })
    }*/
}