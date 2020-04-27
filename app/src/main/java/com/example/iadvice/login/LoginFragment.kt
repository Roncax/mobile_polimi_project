package com.example.iadvice.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.App
import com.example.iadvice.R
import com.example.iadvice.database.AppDatabase
import com.example.iadvice.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.login_fragment.*

private const val TAG = "LoginViewModel" //used for the logs

//TODO use livedata to observe the loginviewmodel
class LoginFragment : Fragment() {
    //private lateinit var binding: LoginFragmentBinding //class created by the compiler for the binding

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
        val userDataSource = AppDatabase.getInstance(application).userDao
        val viewModelFactory = LoginViewModelFactory(userDataSource, application)
        // viewModelProviders used to not destroy the viewmodel until detached
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.apply {
            loginButton.setOnClickListener {
                val user = username_text.text.toString()
                App.user = user
                viewModel.loginUser(
                    binding.passwordText.text.toString(),
                    binding.usernameText.text.toString()
                )
                requireView().findNavController().navigate(R.id.action_loginFragment_to_chatActivity)
            }

            registerButton.setOnClickListener {
                requireView().findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            facebookLoginButton.setOnClickListener {}
            googleLoginButton.setOnClickListener {}
            twitterLoginButton.setOnClickListener {}
        }
        return binding.root
    }


}
