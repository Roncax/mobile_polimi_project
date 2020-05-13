package com.example.iadvice.NewQuestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Switch
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.iadvice.R
import com.example.iadvice.databinding.NewQuestionFragmentBinding
import com.hbb20.CountryCodePicker
import com.hbb20.CountryCodePicker.OnCountryChangeListener


class NewQuestionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: NewQuestionFragmentBinding
    private lateinit var viewModel: NewQuestionViewModel
    var countryCodePicker: CountryCodePicker? = null

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

        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            NewQuestionViewModelFactory(
                application
            )
        // viewModelProviders used to not destroy the viewmodel until detached
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewQuestionViewModel::class.java)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set data to viewModel
        viewModel.durationSpinner = binding.durationSpinner
        viewModel.genderSpinner = binding.genderSpinner
        viewModel.categorySpinner = binding.categorySpinner

        viewModel.fillSPinners()

        //Set all the ItemListener on the spinners
        viewModel.durationSpinner.onItemSelectedListener = this
        viewModel.genderSpinner.onItemSelectedListener = this
        viewModel.categorySpinner.onItemSelectedListener = this
    }

    private fun onCreateNewQuestion() {
        viewModel.title = binding.argumentText.text.toString()
        //TODO make the call for the DB creation
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected: String = parent?.getItemAtPosition(position).toString()
        when(parent?.id){
            R.id.gender_spinner -> viewModel.category = selected
            R.id.duration_spinner -> viewModel.target = selected
            R.id.gender_spinner -> viewModel.duration = selected
        }
    }

    private fun setPoll(){
        val switch: Switch = binding.pollSwitch
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.poll = true
                Toast.makeText(activity,"Poll feature on", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.poll = false
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
            viewModel.selectedCountry = countryCodePicker!!.getSelectedCountryName().toString()
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
