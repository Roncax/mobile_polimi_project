package com.example.iadvice.login

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.iadvice.App
import com.example.iadvice.MainActivity

import com.example.iadvice.R
import com.example.iadvice.chat.ChatActivity
import com.example.iadvice.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.login_fragment.*

private var TAG = "LoginViewModel" //used for the logs

class LoginFragment : Fragment() {
    //private lateinit var binding: LoginFragmentBinding //class created by the compiler for the binding

    private lateinit var viewModel: LoginViewModel

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
                val user = username_text.text.toString()
                App.user = user
                viewModel.loginUserButton(binding.passwordText.text.toString(), binding.usernameText.text.toString())
                view!!.findNavController().navigate(R.id.action_loginFragment_to_chatActivity)
                //startActivity(Intent(MainActivity@ LoginFragment, ChatActivity::class.java))
            }

            registerButton.setOnClickListener {
                viewModel.registerUserButton(binding.passwordText.text, binding.usernameText.text)
            }

            //facebook_login_button.setOnClickListener {}
           // google_login_button.setOnClickListener {}
           // twitter_login_button.setOnClickListener {}
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
