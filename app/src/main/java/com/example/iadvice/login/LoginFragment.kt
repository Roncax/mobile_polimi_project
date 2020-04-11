package com.example.iadvice.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.iadvice.R
import com.example.iadvice.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    //private lateinit var binding: LoginFragmentBinding //class created by the compiler for the binding
    private var TAG = "LoginViewModel" //used for the logs
    private lateinit var viewModel: LoginViewModel


    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<LoginFragmentBinding>(
            inflater,
            R.layout.login_fragment, container, false
        )
        // viewModelProviders used to not destroy the viewmodel until detached
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.apply {
            loginButton.setOnClickListener {
                loginControl(it)
                viewModel.loginUser(binding.passwordText.text, binding.usernameText.text)}
            registerButton.setOnClickListener {
                registerControl(it)
                viewModel.registerUser(binding.passwordText.text, binding.usernameText.text)}
            facebookLoginButton.setOnClickListener { facebookLogin(it) }
            googleLoginButton.setOnClickListener { googleLogin(it)}
            twitterLoginButton.setOnClickListener{twitterLogin(it)}
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun registerControl(it: View?) {
        Log.i(TAG, "Registrazione iniziata")

    }

    private fun loginControl(it: View?) {
        Log.i(TAG, "Login iniziato")
    }

    private fun facebookLogin(it: View?) {
        Log.i(TAG, "Login facebook iniziato")
    }

    private fun twitterLogin(it: View?) {
        Log.i(TAG, "Login twitter iniziato")
    }

    private fun googleLogin(it: View?) {
        Log.i(TAG, "Login google iniziato")
    }

}
