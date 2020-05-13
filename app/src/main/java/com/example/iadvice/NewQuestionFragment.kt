package com.example.iadvice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.iadvice.databinding.NewQuestionFragmentBinding
import com.hbb20.CountryCodePicker
import com.hbb20.CountryCodePicker.OnCountryChangeListener


class NewQuestionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: NewQuestionFragmentBinding

    private lateinit var viewModel: NewQuestionFragmentViewModel


    var countryCodePicker: CountryCodePicker? = null
    lateinit var category: String
    lateinit var gender: String
    lateinit var duration: String
    lateinit var title: String
    lateinit var selectedCountry: String
    var poll: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.new_question_fragment,
            container,
            false
        )

        //bind views
        countryCodePicker = binding.countrySpinner
        binding.countrySpinner.visibility = View.GONE

        //Setting Listeners for views
        setPoll()
        setCountrySwitch()
        onSelectedCountry()
        binding.createButton.setOnClickListener { onCreateNewQuestion() }


        return binding.root
    }




    /**
     * gestione lista scelte menu a tendina. Sarà poi implementata prendendola da DB
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val durationSpinner = binding.durationSpinner
        val genderSpinner = binding.genderSpinner
        val categorySpinner = binding.categorySpinner

        //Set all the ItemListener on the spinners
        durationSpinner.onItemSelectedListener = this
        genderSpinner.onItemSelectedListener = this
        categorySpinner.onItemSelectedListener = this


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            categorySpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.duration_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            durationSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter
        }
    }

    private fun onCreateNewQuestion() {
        title = binding.argumentText.text.toString()
        //TODO make the call for the DB creation
            //if not selected country put a dummy value
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected: String = parent?.getItemAtPosition(position).toString()
        when(parent?.id){
            R.id.gender_spinner -> category = selected
            R.id.duration_spinner -> gender = selected
            R.id.gender_spinner -> duration = selected
        }
    }

    private fun setPoll(){
        val switch: Switch = binding.pollSwitch
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                poll = true
                Toast.makeText(activity,"Poll feature on", Toast.LENGTH_SHORT).show()
            } else {
                poll = false
                Toast.makeText(activity,"Poll feature off", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setCountrySwitch() {
        val switch: Switch = binding.countrySwitch
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.countrySpinner.visibility = View.VISIBLE
            } else {
                binding.countrySpinner.visibility = View.GONE
            }
        }
    }

    private fun onSelectedCountry(){
        countryCodePicker!!.setOnCountryChangeListener(OnCountryChangeListener {
            Toast.makeText( context, "Updated " + countryCodePicker!!.getSelectedCountryName(), Toast.LENGTH_SHORT).show()
            selectedCountry = countryCodePicker!!.getSelectedCountryName().toString()
        })
    }

}
