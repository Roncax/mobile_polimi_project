package com.example.iadvice.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.databinding.SettingsFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hbb20.CountryCodePicker.OnCountryChangeListener
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.register_fragment.add_image_register_button
import kotlinx.android.synthetic.main.settings_fragment.*


private const val TAG = "SettingsFragment"

class SettingsFragment : Fragment() {

    companion object {

        //image URI
        var imageUri: Uri =
            Uri.parse("gs://iadvice-49847.appspot.com/avatar_images/default_picture.png")

    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.settings_fragment, container, false
        )

        // viewModelProviders used to not destroy the viewmodel until detached
        requireNotNull(this.activity).application

        viewModel.getUser()

/*
        binding.apply {
            registerButton.setOnClickListener {
                if (binding.nicknameText.text.toString().isNotEmpty() and
                    binding.ageRegisterText.text.toString().isNotEmpty() and
                    binding.genderSpinner.selectedItem.toString().isNotEmpty() and
                    binding.emailRegisterText.text.toString().isNotEmpty() and
                    binding.firstPwText.text.toString().isNotEmpty()
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
        */


        viewModel.gender.observe(viewLifecycleOwner, Observer { newScore ->
            binding.genderText.text = viewModel.gender.value
        })

        viewModel.age.observe(viewLifecycleOwner, Observer { newAge ->
            binding.ageText.text = viewModel.age.value.toString()
        })

        viewModel.username.observe(viewLifecycleOwner, Observer { newUsername ->
            binding.usernameText.text = viewModel.username.value
        })

        viewModel.country.observe(viewLifecycleOwner, Observer { newCountry ->
            binding.countryText.text = viewModel.country.value
        })


        //Todo email, password e immagine




        binding.apply {
            ageRegisterText.afterTextChanged { viewModel.setAge(it.toInt()) }
            nicknameText.afterTextChanged { viewModel.setUsername(it) }


           genderSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setGender(genderSpinner.selectedItem.toString())
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    //no code here
                }
            })

          countrySpinner.setOnCountryChangeListener(OnCountryChangeListener {
                viewModel.setCountry(countrySpinner.selectedCountryName)
            })

            //Set listener for categories button
                categoriesButton.setOnClickListener {
                    showCategoriesDialog()
                }
        }


        //uploadDefaultImage()
        return binding.root
    }

    //function used to recycle code for the EditText listeners of different fields
    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }


    // Method to show an alert dialog with multiple choice list items
    private fun showCategoriesDialog() {
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog

        // Initialize an array of colors
        //TODO upload from array resource
        val arrayCat = resources.getStringArray(R.array.categories)

        // Initialize a boolean array of checked items
        val arrayChecked = booleanArrayOf(false, false, false, true)


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this.requireContext())

        // Set a title for alert dialog
        builder.setTitle("Choose categories of interest")

        // Define multiple choice items for alert dialog
        builder.setMultiChoiceItems(arrayCat, arrayChecked) { _, which, isChecked ->
            // Update the clicked item checked status
            arrayChecked[which] = isChecked

            // Get the clicked item
            val categories = arrayCat[which]


        }

        // Set the positive/yes button click listener
        builder.setPositiveButton("OK") { _, _ ->

            viewModel.categories = mutableListOf()
            for (i in arrayCat.indices) {
                val checked = arrayChecked[i]
                if (checked) {
                    viewModel.categories.add(arrayCat[i])
                }
            }
        }


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }



    /*
    private fun performRegister(binding: SettingsFragmentBinding) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.emailRegisterText.text.toString(),
            binding.firstPwText.text.toString()
        )
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.e(TAG, "Successfull registration")
                }
                val uid = it.result!!.user!!.uid
                Log.d(TAG, "Successfull created user with uid: ${uid}")

                viewModel.username = binding.nicknameText.text.toString()
                viewModel.uid = uid
                viewModel.age = binding.ageRegisterText.text.toString().toInt()
                viewModel.gender = binding.genderSpinner.selectedItem.toString()
                viewModel.uri = imageUri
                viewModel.country = binding.countrySpinner.selectedCountryName

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
*/

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUri = data?.data!!
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            Glide.with(this@SettingsFragment)
                .load(imageUri)
                .fitCenter()
                .circleCrop()
                .into(add_image_register_button)
        }
    }

    private fun uploadDefaultImage() {
        val imagesRef: StorageReference? =
            FirebaseStorage.getInstance().reference.child("avatar_images/default_picture.png")

        binding.apply {
            GlideApp.with(this@SettingsFragment)
                .load(imagesRef)
                .fitCenter()
                .circleCrop()
                .into(addImageRegisterButton)
        }

    }


}