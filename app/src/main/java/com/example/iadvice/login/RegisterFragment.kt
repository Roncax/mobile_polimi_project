package com.example.iadvice.login

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.databinding.RegisterFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.register_fragment.*
import kotlin.properties.Delegates

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {


    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: RegisterFragmentBinding

    private var isTablet by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.register_fragment, container, false
        )

        // viewModelProviders used to not destroy the viewmodel until detached
        requireNotNull(this.activity).application

        isTablet = context?.resources?.getBoolean(R.bool.isTablet)!!
        if(!isTablet) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }


        binding.apply {
            registerButton.setOnClickListener {
                if (binding.nicknameText.text.toString().isNotEmpty() and
                    binding.genderSpinner.selectedItem.toString().isNotEmpty() and
                    binding.emailRegisterText.text.toString().isNotEmpty() and
                    binding.firstPwText.text.toString().isNotEmpty() and
                    (viewModel.categories != mutableListOf<String>()) and
                    (viewModel.uri != Uri.parse("gs://iadvice-49847.appspot.com/avatar_images/default_picture.png"))
                ) {
                    performRegister(binding)
                } else {
                    Toast.makeText(
                        context, "You forgot a field, please fill and retry",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

            }

            addImageRegisterButton.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
            }

            categoriesButton.setOnClickListener {
                showCategoriesDialog()
            }

        }
        uploadDefaultImage()
        return binding.root
    }

    // Method to show an alert dialog with multiple choice list items
    private fun showCategoriesDialog() {
        lateinit var dialog: AlertDialog

        val arrayCat = resources.getStringArray(R.array.categories)
        val arrayChecked = BooleanArray(arrayCat.size) { i -> arrayCat[i] == "Casual" }
        val builder = MaterialAlertDialogBuilder(this.requireContext())

        builder.setTitle("Categories of interest")

        // Define multiple choice items for alert dialog
        builder.setMultiChoiceItems(arrayCat, arrayChecked) { _, which, isChecked ->
            arrayChecked[which] = isChecked
        }

        builder.setPositiveButton("Done") { _, _ ->
            viewModel.categories = mutableListOf()
            for (i in arrayCat.indices) {
                val checked = arrayChecked[i]
                if (checked) {
                    viewModel.categories.add(arrayCat[i])
                }
            }
        }

        dialog = builder.create()
        dialog.show()
    }


    private fun performRegister(binding: RegisterFragmentBinding) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.emailRegisterText.text.toString(),
            binding.firstPwText.text.toString()
        )
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.e(TAG, "Successfull registration")
                }

                viewModel.username = binding.nicknameText.text.toString()
                viewModel.uid = it.result!!.user!!.uid
                viewModel.gender = binding.genderSpinner.selectedItem.toString()
                viewModel.country = binding.countrySpinner.selectedCountryCode

                viewModel.registerUser()
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
            }

        if (!requireView().findNavController().popBackStack()) {
            Toast.makeText(
                context, "Cannot go back to login, error!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            viewModel.uri = data.data!!

            if (resultCode == Activity.RESULT_OK && requestCode == 100) {
                Glide.with(this@RegisterFragment)
                    .load(viewModel.uri)
                    .fitCenter()
                    .circleCrop()
                    .into(add_image_register_button)
            }
        }
    }

    private fun uploadDefaultImage() {
        val imagesRef: StorageReference =
            FirebaseStorage.getInstance().reference.child("avatar_images/default_picture.png")

        binding.apply {
            GlideApp.with(this@RegisterFragment)
                .load(imagesRef)
                .fitCenter()
                .circleCrop()
                .into(addImageRegisterButton)
        }

    }


}