package com.example.iadvice.settings

import android.app.Service
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.SettingsFragmentBinding
import com.example.iadvice.home.HomeFragment
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


        //Todo prima di tutto setto tutto come non editabile, e gli metto come testo il valore attuale

        /*binding.apply {
            ageRegisterText.isFocusable = false
            ageRegisterText.isClickable = false
            nicknameText.isFocusable = false
            nicknameText.isClickable = false
            genderSpinner.isEnabled = false
            binding.countrySpinner.setCcpClickable(false)
        }*/
        allowClickability(false)


        //todo scarico i valori attuali
        viewModel.getUser()

        viewModel.age.observe(viewLifecycleOwner, Observer { newAge ->
            binding.ageRegisterText.setText(viewModel.age.value.toString())
        })

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
            //todo capire come mettere il valore attuale
            // binding.countrySpinner.setCountryForNameCode("IT")

        })

        /*
        viewModel.categories = mutableListOf()
        for (i in arrayCat.indices) {
            val checked = arrayChecked[i]
            if (checked) {
                viewModel.categories.add(arrayCat[i])
            }
        }
*/


/*
        binding.apply {
            registerButton.setOnClickListener {
                if (binding.nicknameText.text.toString().isNotEmpty() and
                    binding.ageRegisterText.text.toString().isNotEmpty() and
                    binding.genderSpinner.selectedItem.toString().isNotEmpty()
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


/*
        viewModel.age.observe(viewLifecycleOwner, Observer { newAge ->
            binding.ageText.text = viewModel.age.value.toString()
        })

        viewModel.username.observe(viewLifecycleOwner, Observer { newUsername ->
            binding.usernameText.text = viewModel.username.value
        })

        viewModel.country.observe(viewLifecycleOwner, Observer { newCountry ->
            binding.countryText.text = viewModel.country.value
        })

 */



/*
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

            /*Set listener for categories button
                categoriesButton.setOnClickListener {
                    showCategoriesDialog()
                }
             */
        }
*/

        //uploadDefaultImage()



        binding.apply {
            registerButton.setOnClickListener {
                (viewAdapter as CategoriesAdapter).setClickable(true)

                ageRegisterText.setFocusableInTouchMode(true)
                registerButton.text = "APPLY CHANGES"
                allowClickability(true)

                //todo hai cliccato che vuoi modificare
                viewModel.setUsername(binding.nicknameText.text.toString())
                viewModel.setAge(binding.ageRegisterText.text.toString().toInt())
                viewModel.setGender(binding.genderSpinner.selectedItem.toString())

               // checkSelectedCategories()







                //todo cambia scritta bottone e tira su tutti i nuovi valori
                //todo passa tutto al viewModel che fa l'upload!
            }
        }



        return binding.root
    }

    private fun allowClickability(bool: Boolean) {
        binding.apply {
            ageRegisterText.isFocusable = bool
            ageRegisterText.isClickable = bool
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



        /* Initialize a boolean array of checked items
        val arrayChecked = booleanArrayOf(true, false, false, true)

        viewModel.categoriesList = mutableListOf()
        for (i in arrayCat.indices) {
            val checked = arrayChecked[i]
            if (checked) {
                viewModel.categoriesList.add(arrayCat[i])
            }
        }
        */


        /*todo setta tutto a false
        var categoriesList: MutableMap<String, Boolean> = mutableMapOf<String, Boolean>()
        for (i in arrayCat.indices){
            categoriesList.put(arrayCat[i],false)
        }

        categoriesList.putAll(viewModel.categoriesList)
        */


/*
        val arrayCat = resources.getStringArray(R.array.categories)
        val arrayChecked = booleanArrayOf(true, false, false, true)
        //val arrayChecked: MutableCollection<String> = viewModel.categoriesList.values

        //todo mutablemap con tutto settato a false
        var all: MutableMap<String, String> = mutableMapOf<String, String>()
        for (i in arrayCat.indices){
            all.put(arrayCat[i], "false")
        }
        all.putAll(viewModel.categoriesList)
*/


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


    override fun onItemClick(item: String) {
        //Todo proviamo a cambiare il valore del boolean
    }


}

