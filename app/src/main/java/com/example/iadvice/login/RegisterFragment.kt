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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.register_fragment.*
import java.io.File
import java.io.FileInputStream


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

            add_image_register_button.setOnClickListener {
                uploadImage()

            }
            facebookRegisterButton.setOnClickListener { }
            googleRegisterButton.setOnClickListener { }
            twitterRegisterButton.setOnClickListener { }
        }

        return binding.root
    }

    private fun uploadImage() {
        val storage = FirebaseStorage.getInstance().reference
        val imagesRef: StorageReference = storage.child("avatar_images")
        val stream = FileInputStream(File("path/to/images/rivers.jpg"))

        val uploadTask = imagesRef.putStream(stream)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

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
                if (!it.isSuccessful) {
                    Log.e(TAG,"La registrazione non é andata a buon fine")
                }
                var uid = it.result!!.user!!.uid
                Log.d(TAG, "Successfull created user with uid: ${uid}")
                viewModel.registerUser(
                    username = binding.nicknameText.text.toString(),
                    uid = uid,
                    age = binding.ageRegisterText.text.toString().toInt(),
                    gender = gender
                )
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
        requireView().findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

    }

}