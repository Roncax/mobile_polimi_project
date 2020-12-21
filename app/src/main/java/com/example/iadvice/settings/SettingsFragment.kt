package com.example.iadvice.settings

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.databinding.SettingsFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.evaluation_dialog.view.*
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.settings_fragment.*


private const val TAG = "SettingsFragment"

class SettingsFragment : Fragment(),  OnCategoryClickListener {

    companion object {
        //image URI
        var imageUri: Uri =
            Uri.parse("gs://iadvice-49847.appspot.com/avatar_images/default_picture.png")
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsFragmentBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    //private lateinit var viewAdapter: RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>


    private val categoryObserver = Observer<MutableMap<String,String>> { category ->
        Log.d(TAG, "new category updated: '${category}' ")

        attachAdapter()
    }

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
        //todo capire come mettere il valore attuale col nome del paese e non con il codice
            // binding.countrySpinner.setCountryForNameCode("IT")
        })

        //setto i valori attuali
        viewModel.getUser()





        binding.apply {
            addImageRegisterButton.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
            }

        }

        //todo da chiamare
        //uploadDefaultImage()



        binding.apply {
            editButton.setOnClickListener {
                (viewAdapter as CategoriesAdapter).setClickable(true)
                nicknameText.setFocusableInTouchMode(true)
                allowClickability(true)

                editButton.setText(R.string.apply_changes)

                if( editButton.text.toString() == "APPLY CHANGES" ){ //se uso R.string.apply_changes.toString() non entra
                    viewModel.setUsername(binding.nicknameText.text.toString())
                    viewModel.setGender(binding.genderSpinner.selectedItem.toString())
                    viewModel.setCountry(binding.countrySpinner.selectedCountryName.toString())
                    //todo torna a pagina principale
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


    /*    //todo fare upload immagine nuova
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
*/

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
        //Todo tira su il valore del checkbox

        if(viewModel.categoriesList.containsKey(item)){
            viewModel.categoriesList.remove(item)
        }
        else{
            viewModel.categoriesList.put(item,"true")
        }
    }
}

