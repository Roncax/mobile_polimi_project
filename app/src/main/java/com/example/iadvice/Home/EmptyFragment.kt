package com.example.iadvice.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.iadvice.R

class EmptyFragment: Fragment() {

    companion object {
        const val TAG = "EmptyFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.empty_right, container,false)
    }

}