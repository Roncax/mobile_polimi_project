package com.example.iadvice.NewQuestion

import android.app.Application
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.database.Poll
import com.example.iadvice.login.LoginFragment.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class NewQuestionViewModel(private val application: Application) : ViewModel() {

    /*TODO mettere in LiveData
    lateinit var category: String
    lateinit var target: String
    lateinit var duration: String
    lateinit var title: String
    lateinit var selectedCountry: String
    var poll: Boolean = false
*/

    private val _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category

    private val _target = MutableLiveData<String>()
    val target: LiveData<String>
        get() = _target

    private val _duration = MutableLiveData<String>()
    val duration: LiveData<String>
        get() = _duration

    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    private val _selectedCountry = MutableLiveData<String>()
    val selectedCountry: LiveData<String>
        get() = _selectedCountry

    private val _poll = MutableLiveData<Boolean>()
    val poll: LiveData<Boolean>
        get() = _poll


    /** for the visibility of the country selector */
    private val _visibility = MutableLiveData<Boolean>()
    val visibility: LiveData<Boolean>
        get() = _visibility


    init {
        _poll.value = false
        _visibility.value = false
    }


    lateinit var durationSpinner: Spinner
    lateinit var genderSpinner: Spinner
    lateinit var categorySpinner: Spinner
    lateinit var question: String


    /**
     * fill in the options of the spinners
     * TODO it will be implemented by taking values from the DB
     */
    fun fillSPinners() {
        //category spinner
        ArrayAdapter.createFromResource(
            application,
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            categorySpinner.adapter = adapter
        }
        //durationSpinner
        ArrayAdapter.createFromResource(
            application,
            R.array.duration_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            durationSpinner.adapter = adapter
        }
        //genderSpinner
        ArrayAdapter.createFromResource(
            application,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter
        }
    }


    fun onShowCountry(isChecked: Boolean) {
        _visibility.value = isChecked
    }

    fun onShowPoll(isChecked: Boolean) {
        _poll.value = isChecked
    }

    fun onSelectedCountry(selectedCountry: String) {
        _selectedCountry.value = selectedCountry
    }

    fun onItemSelected(parent: AdapterView<*>?, selectedItem: String) {
        when (parent?.id) {
            R.id.gender_spinner -> _category.value = selectedItem
            R.id.duration_spinner -> _target.value = selectedItem
            R.id.gender_spinner -> _duration.value = selectedItem
        }

    }

    fun onCreateNewQuestion() {

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val mDatabase: DatabaseReference
        val userlist: MutableList<String> = mutableListOf()

        mDatabase = FirebaseDatabase.getInstance().reference


        //TODO: ogni volta che vuoi aggiungere ad una lista devi prima rileggerla tutta da firebase, aggiungere e ricaricare
        //TODO potrebbe essere troppo pesante per quello che dobbiamo fare...in quel caso ogni elemento diventa un child e poi usare childByAutoId

        val key = mDatabase.child("chats").push().key
        Log.i("NEW KEY","${key}")

       // val question = _title.value.toString() //TODO non viene settato il titolo dall'altra parte
        //val question = "una questione privata"

        userlist.add(userId)
        val poll = Poll(question, userId) //todo implementare seriamente
        val chatid = key!!
        val  isActive = true
        val newChat = Chat(chatid, userId, question, poll, isActive, userlist)

        mDatabase.child("chats").child(key).setValue(newChat)
        mDatabase.child("users").child(userId).child("chatlist").child("your").child(key).setValue(key)
    }
}

