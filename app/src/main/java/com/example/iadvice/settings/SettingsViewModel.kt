package com.example.iadvice.settings

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iadvice.database.User
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
    lateinit var categories: MutableList<String>




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


    init{
        _age.value = -1
    }

    fun setAge(age: Int){
        _age.value = age
       // updateUser()
    }

    fun setUsername(username: String){
        _username.value = username
        updateUser()
    }

    fun setGender(gender: String){
        _gender.value = gender
        updateUser()
    }

    fun setCountry(country: String){
        _country.value = country
        updateUser()
    }


     // update user with the selected parameters
    fun updateUser() {
        val db = Firebase.database.reference

        val user = User(
            uid = userId,
            username = _username.value.toString(),
            gender = _gender.value.toString(),
            age = 5,
            country = _country.value.toString()
        )

        db.child("users").child(userId).setValue(user)


        for (category in categories) {
            db.child("users").child(userId).child("categories").child(category).setValue("true")
        }

         /*
        FirebaseStorage.getInstance().reference.child("avatar_images/$userId").putFile(uri)
         */

     }



    fun getUser(){

        userId = FirebaseAuth.getInstance().uid!!

        val userListener = FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)


        userListener.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.e(TAG, "Cancelled ids retrieve on fetch user '$userId'")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>()
                Log.d(TAG, "user '${user!!.uid}' retrieved")

                _gender.value = user!!.gender
                _username.value = user!!.username
                _age.value = user!!.age.toInt()
                _country.value = user!!.country
                //TODO  gestione categories e uri e password!!!

                }
        })
    }


}