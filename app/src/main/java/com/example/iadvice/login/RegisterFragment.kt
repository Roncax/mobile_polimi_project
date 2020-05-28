package com.example.iadvice.login

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
                performRegister(binding)

            }
            facebookRegisterButton.setOnClickListener {  }
            googleRegisterButton.setOnClickListener { }
            twitterRegisterButton.setOnClickListener { }
        }

        return binding.root
    }


fun performRegister(binding: RegisterFragmentBinding) {
    //TODO handle empty fields

    val gender: String

        if (binding.genderChoice.isChecked) {
            gender = "female"
        } else {
            gender = "male"
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.emailRegisterText.text.toString(),
            binding.firstPwText.text.toString()
        )
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                var uid = it.result!!.user!!.uid
                Log.d(TAG, "Successfull created user with uid: ${uid}")
                viewModel.registerUser(
                    username = binding.nicknameText.text.toString(),
                    uid = uid,
                    age = binding.ageRegisterText.text.toString().toInt(),
                    gender = gender
                )
            }
            .addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
        requireView().findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

}

}