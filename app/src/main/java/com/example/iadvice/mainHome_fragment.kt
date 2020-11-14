package com.example.iadvice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

const val TAG = "MAIN_HOME_FRAGMENT"
class mainHome_fragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            Log.d(TAG, "The user is already present with id ${user.uid}")
            findNavController().navigate(R.id.homeFragment)
        } else {
            Log.d(TAG, "The user is not already present")
            findNavController().navigate(R.id.loginFragment)
        }
    }
}