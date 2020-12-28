package com.example.iadvice.settings

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.iadvice.GlideApp
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.example.iadvice.databinding.SettingsFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.evaluation_dialog.view.*
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.settings_fragment.*


private const val TAG = "SettingsFragment"

class SettingsFragment : Fragment(),  OnCategoryClickListener {


    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsFragmentBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>


    private val categoryObserver = Observer<MutableMap<String,String>> { category ->
        Log.d(TAG, "new category updated: '${category}' ")

        attachAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.settings_fragment, container, false
        )
        // viewModelProviders used to not destroy the viewmodel until detached
        requireNotNull(this.activity).application

        allowClickability(false)


      //set listeners
        viewModel.categoriesListLiveData.observe(viewLifecycleOwner, categoryObserver)

        viewModel.username.observe(viewLifecycleOwner, Observer { newUsername ->
            binding.nicknameText.setText(viewModel.username.value)
        })

        viewModel.gender.observe(viewLifecycleOwner, Observer { newGender ->
            when (newGender) {
                "Male" -> binding.genderSpinner.setSelection(0)
                "Female" -> binding.genderSpinner.setSelection(1)
            }
        })

        viewModel.country.observe(viewLifecycleOwner, Observer { newCountry ->
             binding.countrySpinner.setCountryForNameCode(PersistenceUtils.currentUser.country)
        })

        viewModel.getUser()
        binding.apply {
            addImageRegisterButton.setOnClickListener {
                if (viewModel.editorMode) {
                    val gallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, 100)
                }
            }

        }

        uploadDefaultImage()

        binding.apply {
            editButton.setOnClickListener {

                if(viewModel.editorMode ){ //se uso R.string.apply_changes.toString() non entra
                    viewModel.setUsername(binding.nicknameText.text.toString())
                    viewModel.setGender(binding.genderSpinner.selectedItem.toString())
                    viewModel.setCountry(binding.countrySpinner.selectedCountryNameCode.toString())

                    viewModel.updateUser()
                    viewModel.editorMode = false

                    PersistenceUtils.retrieveCurrentUserImage()

                    // Pop the stack and go back to home
                    if (!requireView().findNavController().popBackStack()) {
                        Toast.makeText(
                            context, "Cannot go back to login, error!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else{
                    (viewAdapter as CategoriesAdapter).setClickable(true)
                    nicknameText.isFocusableInTouchMode = true
                    allowClickability(true)
                    editButton.setText(R.string.apply_changes)
                    viewModel.editorMode = true
                }
                return@setOnClickListener
            }
        }

        return binding.root
    }

    private fun allowClickability(bool: Boolean) {
        binding.apply {
            nicknameText.isFocusable = bool
            nicknameText.isClickable = bool
            genderSpinner.isEnabled = bool
            binding.countrySpinner.setCcpClickable(bool)
        }
    }


    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            viewModel.uri = data.data!!
            if (resultCode == Activity.RESULT_OK && requestCode == 100) {

                GlideApp.with(this@SettingsFragment)
                    .load(viewModel.uri)
                    .fitCenter()
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.addImageRegisterButton)
            }
        }
    }

    private fun uploadDefaultImage() {

        GlideApp.with(this@SettingsFragment)
            .load(PersistenceUtils.currentUserImage)
            .fitCenter()
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.addImageRegisterButton)

    }


    private fun attachAdapter() {

        //array che contiene tutte le categorie
        val arrayCat = resources.getStringArray(R.array.categories)
        //array che contiene solo le categorie selezionate
        val categories = viewModel.categoriesList.keys
        //array che mappa categoria con il fatto che sia selezionata o meno
        val arrayChecked = BooleanArray(arrayCat.size){false}

       for (c in categories){
           val index = arrayCat.indexOf(c)
           arrayChecked[index] = true
       }

        viewAdapter = CategoriesAdapter(arrayCat, arrayChecked, this@SettingsFragment)

        (viewAdapter as CategoriesAdapter).setClickable(false)

        recyclerView = binding.RecyclerView.apply {
            //used to improve performances
            setHasFixedSize(true)
            adapter = viewAdapter
        }

    }


    override fun onItemClick(item: String, clicked: Boolean){
        if(viewModel.categoriesList.containsKey(item)){
            viewModel.categoriesList.remove(item)
        }
        else{
            viewModel.categoriesList[item] = "true"
        }
    }
}

