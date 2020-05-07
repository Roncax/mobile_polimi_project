package com.example.iadvice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.iadvice.databinding.NewQuestionFragmentBinding


class NewQuestionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: NewQuestionFragmentBinding

    private lateinit var viewModel: NewQuestionFragmentViewModel

    lateinit var category: String
    lateinit var gender: String
    lateinit var duration: String
    lateinit var title: String
    var anonimity: Boolean = false


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

        binding.createButton.setOnClickListener { onCreateNewQuestion() }
        setAnonimity()

        return binding.root
    }


    /**
     * gestione lista scelte menu a tendina. Sarà poi implementata prendendola da DB
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categorySpinner = binding.categorySpinner
        val durationSpinner = binding.durationSpinner
        val genderSpinner = binding.genderSpinner

        //Set all the ItemListener on the spinners
        categorySpinner.onItemSelectedListener = this
        durationSpinner.onItemSelectedListener = this
        genderSpinner.onItemSelectedListener = this


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
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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


    /**
     * make the call to the DB
     */
    private fun onCreateNewQuestion() {
        title = binding.boxTitle.text.toString()
        //TODO make the call for the DB creation
    }




    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected: String = parent?.getItemAtPosition(position).toString()
        when(parent?.id){
            R.id.category_spinner -> category = selected
            R.id.duration_spinner -> gender = selected
            R.id.gender_spinner -> duration = selected
        }
    }

    private fun setAnonimity(){

        val switch: Switch = binding.anonimitySwitch
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                anonimity = true
                Toast.makeText(activity,"Your question will be anonymous", Toast.LENGTH_SHORT).show()
            } else {
                anonimity = false
                Toast.makeText(activity,"Involved users will see your identity", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
