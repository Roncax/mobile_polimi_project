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
import java.net.URI

private const val TAG = "SETTINGS_VIEWMODEL"

class SettingsViewModel : ViewModel() {

    lateinit var userId: String
    var uri =  Uri.parse("gs://iadvice-49847.appspot.com/avatar_images/default_picture.png")
    var editorMode: Boolean = false

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

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun setCountry(country: String) {
        _country.value = country
    }

    // update user with the selected parameters
    fun updateUser() {
        val db = Firebase.database.reference
        

        db.child("users").child(userId).child("country").setValue(_country.value.toString())
        db.child("users").child(userId).child("gender").setValue(_gender.value.toString())
        db.child("users").child(userId).child("username").setValue(_username.value.toString())

        for (category in categoriesList.keys) {
            db.child("users").child(userId).child("categories").child(category).setValue("true")
        }

        if (uri != Uri.parse("gs://iadvice-49847.appspot.com/avatar_images/default_picture.png")) {
            Log.d(TAG, "L'immagine non é nulla!!")
            FirebaseStorage.getInstance().reference.child("avatar_images/$userId").putFile(uri)
        }
    }


    fun getUser() {
        userId = PersistenceUtils.currentUser.uid
        _gender.value = PersistenceUtils.currentUser.gender
        _username.value = PersistenceUtils.currentUser.username
        _country.value = PersistenceUtils.currentUser.country
        categoriesList = PersistenceUtils.currentUser.categories
        categoriesListLiveData.setValue(categoriesList)
    }

}