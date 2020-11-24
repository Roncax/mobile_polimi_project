package com.example.iadvice

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth


class MainHomeFragment : Fragment() {

    companion object{
        const val TAG = "MAIN_HOME_FRAGMENT"
    }

    override fun onResume() {
        super.onResume()
        val user = FirebaseAuth.getInstance().currentUser

        /**
         * Choose what fragment upload first
         */
        if (user != null) {
            Log.d(TAG, "The user is already present with id ${user.uid}")
            PersistenceUtils.retrieveUser()
            PersistenceUtils.retrieveCurrentUserImage()
            findNavController().navigate(R.id.homeFragment)
        } else {
            Log.d(TAG, "The user is not already present")
            findNavController().navigate(R.id.loginFragment)
        }
    }
}