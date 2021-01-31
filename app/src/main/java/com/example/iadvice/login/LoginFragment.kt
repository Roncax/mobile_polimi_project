package com.example.iadvice.login

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.R
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.databinding.LoginFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates


class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
    }

    private lateinit var viewModel: LoginViewModel

    private var isTablet by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // bind the login_fragment layout with the binding variable
        val binding = DataBindingUtil.inflate<LoginFragmentBinding>(
            inflater,
            R.layout.login_fragment, container, false
        )

        // viewModelProviders used to not destroy the viewModel until detached
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        //Force the screen orientation in case of smartphone
        isTablet = context?.resources?.getBoolean(R.bool.isTablet)!!
        if(!isTablet)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        binding.apply {

            loginButton.setOnClickListener {
                val password = binding.passwordText.text.toString()
                val email = binding.usernameText.text.toString()
                if (password.isEmpty()){
                    Toast.makeText(
                        context, "Password field cannot be empty, try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (email.isEmpty()){
                    Toast.makeText(
                        context, "Email field cannot be empty, try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                performLogin(email, password)
            }

            registerButton.setOnClickListener {
                requireView().findNavController()
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }

            return binding.root
        }
    }

    private fun performLogin(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "signInWithCustomToken:success")
                PersistenceUtils.retrieveUser()
                PersistenceUtils.retrieveCurrentUserImage()

                if (!requireView().findNavController().popBackStack()) {
                    Toast.makeText(
                        this.context, "Go back from login",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Toast.makeText(
                    this.context, "Login successful",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener {
                Toast.makeText(
                    this.context, "Authentication failed, try again.",
                    Toast.LENGTH_SHORT
                ).show()

            }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.GONE
    }



}
