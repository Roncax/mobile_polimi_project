package com.example.iadvice.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.App
import com.example.iadvice.R
import com.example.iadvice.databinding.LoginFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // bind the login_fragment layout with the binding variable
        val binding = DataBindingUtil.inflate<LoginFragmentBinding>(
            inflater,
            R.layout.login_fragment, container, false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = LoginViewModelFactory(application)
        // viewModelProviders used to not destroy the viewmodel until detached
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.apply {

            loginButton.setOnClickListener {
                val password = binding.passwordText.text.toString()
                val email = binding.usernameText.text.toString()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        var uid = it.result!!.user!!.uid
                        Log.d(TAG, "Successfull logged user with uid: ${uid}")
                        viewModel.uploadUser(uid)
                        requireView().findNavController()
                            .navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Failed to login user: ${it.message}")
                    }
            }

                registerButton.setOnClickListener {
                    requireView().findNavController()
                        .navigate(R.id.action_loginFragment_to_registerFragment)
                }

                facebookLoginButton.setOnClickListener {}
                googleLoginButton.setOnClickListener {}
                twitterLoginButton.setOnClickListener {}

            return binding.root
        }
    }


        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            requireActivity()!!.findViewById<AppBarLayout>(R.id.appBarLayout)
                .setVisibility(View.GONE)
        }


    }
