package com.example.iadvice

import android.widget.Spinner
import androidx.lifecycle.ViewModel

class NewQuestionFragmentViewModel: ViewModel() {

    val title: String = ""
    val category: String = ""
    val target: String = ""
    val identity: Boolean = true
    val duration: Int = 24  //durability of the new question (in hours)

}