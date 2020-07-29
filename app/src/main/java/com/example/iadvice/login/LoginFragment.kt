package com.example.iadvice.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.iadvice.R
import com.example.iadvice.databinding.LoginFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth


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
                // TODO DA RIMETTERE
                val password = binding.passwordText.text.toString()
                val email = binding.usernameText.text.toString()

/*
                val password = "123456"
                val email = "tasca@gmail.com"
*/

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        var uid = it.result!!.user!!.uid
                        Log.d(TAG, "Successfull logged user with uid: ${uid}")

                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
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
