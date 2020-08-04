package com.example.iadvice.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.R
import com.example.iadvice.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.register_fragment.*


//TODO aggiungere possibilitá di mettere dentro immagine personale
class RegisterFragment : Fragment() {

    companion object {
        const val TAG = "RegisterFragment"
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
        //image URI
        lateinit var imageUri: Uri
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val binding = DataBindingUtil.inflate<RegisterFragmentBinding>(
            inflater,
            R.layout.register_fragment, container, false
        )

        // viewModelProviders used to not destroy the viewmodel until detached
        val application = requireNotNull(this.activity).application

        binding.apply {
            registerButton.setOnClickListener {
                performRegister(binding)

            }

            addImageRegisterButton.setOnClickListener {
                //BUTTON CLICK
                //check runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(application.applicationContext , Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED
                    ) {
                        //permission denied
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    //system OS is < Marshmallow
                    pickImageFromGallery();
                }
            }



            facebookRegisterButton.setOnClickListener { }
            googleRegisterButton.setOnClickListener { }
        }

        return binding.root
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
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
                    gender = gender,
                    uri = imageUri
                )
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
        requireView().findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageUri = data!!.data!!
            add_image_register_button.setImageURI(imageUri)
        }
    }


}