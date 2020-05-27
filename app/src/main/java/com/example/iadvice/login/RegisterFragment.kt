package com.example.iadvice.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.R
import com.example.iadvice.databinding.RegisterFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

//TODO aggiungere possibilitá di mettere dentro immagine personale
class RegisterFragment : Fragment() {

    companion object {
        const val TAG = "RegisterFragment"
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<RegisterFragmentBinding>(
            inflater,
            R.layout.register_fragment, container, false
        )

        // viewModelProviders used to not destroy the viewmodel until detached
        val application = requireNotNull(this.activity).application
        val viewModelFactory = LoginViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.apply {
            registerButton.setOnClickListener {
                //userRegistration(it)
                val gender: String
                //TODO add personal photo handling
                if (binding.firstPwText.text.toString() == binding.secondPwText.text.toString()) {
                    if (binding.genderChoice.isChecked) { gender = "female" } else { gender = "male" }
                    viewModel.registerUser(
                        username = binding.nicknameText.text.toString(),
                        email = binding.emailRegisterText.text.toString(),
                        password = binding.firstPwText.text.toString(),
                        age = binding.ageRegisterText.text.toString().toInt(),
                        gender = gender
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
        requireView().findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}