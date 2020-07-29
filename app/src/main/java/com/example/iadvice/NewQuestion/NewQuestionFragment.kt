package com.example.iadvice.NewQuestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.iadvice.R
import com.example.iadvice.databinding.NewQuestionFragmentBinding


class NewQuestionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: NewQuestionFragmentBinding
    private lateinit var viewModel: NewQuestionViewModel

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


        //Setting Listeners
        binding.createButton.setOnClickListener { onCreateNewQuestion() }
        binding.countrySpinner.setOnCountryChangeListener { onSelectedCountry() }

        binding.countrySwitch.setOnCheckedChangeListener {  _, isChecked ->
            onShowCountry() }

        /* Setting up LiveData observation relationship */
        viewModel.visibility.observe(viewLifecycleOwner, Observer { isChecked ->
            if (isChecked) {
                binding.countrySpinner.visibility = View.VISIBLE
            } else {
                binding.countrySpinner.visibility = View.GONE
            }
        })

        binding.pollSwitch.setOnCheckedChangeListener { _, isChecked ->
            onShowPoll() }

        viewModel.poll.observe(viewLifecycleOwner, Observer { isChecked ->
            if (isChecked) {
                Toast.makeText(activity,"Poll feature on", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity,"Poll feature off", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set data to viewModel
        viewModel.durationSpinner = binding.durationSpinner
        viewModel.genderSpinner = binding.genderSpinner
        viewModel.categorySpinner = binding.categorySpinner
        viewModel.fillSPinners()

        //TODO implement the interface on the ViewModel and move there the Spinners listener
        viewModel.durationSpinner.onItemSelectedListener = this
        viewModel.genderSpinner.onItemSelectedListener = this
        viewModel.categorySpinner.onItemSelectedListener = this
    }


    private fun onShowCountry() {
        viewModel.onShowCountry( binding.countrySwitch.isChecked)
    }

    private fun onShowPoll() {
        viewModel.onShowPoll( binding.pollSwitch.isChecked)
    }

    private fun onSelectedCountry(){
        viewModel.onSelectedCountry(binding.countrySpinner!!.getSelectedCountryName().toString())
        Toast.makeText( context, "Updated " + binding.countrySpinner!!.getSelectedCountryName(), Toast.LENGTH_SHORT).show()
    }

    /**
     * used for element selected inside the spinners
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected: String = parent?.getItemAtPosition(position).toString()
        viewModel.onItemSelected(parent,selected)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun onCreateNewQuestion() {
        viewModel.question = binding.boxArgument.text.toString()
        viewModel.onCreateNewQuestion()
    }
}
