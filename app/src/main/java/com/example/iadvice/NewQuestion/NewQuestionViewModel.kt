package com.example.iadvice.NewQuestion

import android.app.Application
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import com.example.iadvice.R


class NewQuestionViewModel(private val application: Application) : ViewModel() {

    //TODO mettere in LiveData
    lateinit var category: String
    lateinit var target: String
    lateinit var duration: String
    lateinit var title: String
    lateinit var selectedCountry: String
    var poll: Boolean = false


    lateinit var durationSpinner: Spinner
    lateinit var genderSpinner: Spinner
    lateinit var categorySpinner: Spinner


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
}

