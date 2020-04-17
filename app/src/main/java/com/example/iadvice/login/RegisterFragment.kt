package com.example.iadvice.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.iadvice.R
import com.example.iadvice.databinding.RegisterFragmentBinding

//TODO aggiungere possibilitá di mettere dentro immagine personale
class RegisterFragment : Fragment() {

    private var TAG = "LoginViewModel" //used for the logs
    private lateinit var viewModel: LoginViewModel

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<RegisterFragmentBinding>(
            inflater,
            R.layout.register_fragment, container, false
        )
        // viewModelProviders used to not destroy the viewmodel until detached
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.apply {
            registerButton.setOnClickListener {
                userRegistration(it)
                if (binding.firstPwText.toString() == binding.secondPwText.toString()) {
                    Log.i(TAG, "Password accettate, inizio il login")
                    viewModel.registerUser(
                        binding.nameRegisterText.text.toString(),
                        binding.nicknameText.text.toString(),
                        binding.emailRegisterText.text.toString(),
                        binding.firstPwText.text.toString(),
                        "foto personale"
                    )
                } else {
                    Log.i(TAG, "Le due password non sono identiche")
                }

            }
            facebookRegisterButton.setOnClickListener { facebookRegister(it) }
            googleRegisterButton.setOnClickListener { googleRegister(it) }
            twitterRegisterButton.setOnClickListener { twitterRegister(it) }
        }

        return binding.root
    }

    private fun twitterRegister(it: View?) {
        TODO("Not yet implemented")
    }

    private fun googleRegister(it: View?) {
        TODO("Not yet implemented")
    }

    private fun facebookRegister(it: View?) {
        TODO("Not yet implemented")
    }

    private fun userRegistration(it: View?) {
        TODO("Not yet implemented")
    }
}