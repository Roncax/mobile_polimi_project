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

    lateinit var gender: String
    lateinit var duration: String
    lateinit var title: String
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

        binding.createButton.setOnClickListener { onCreateNewQuestion() }
        setPoll()

        return binding.root
    }


    /**
     * gestione lista scelte menu a tendina. Sarà poi implementata prendendola da DB
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val durationSpinner = binding.durationSpinner
        val genderSpinner = binding.genderSpinner

        //Set all the ItemListener on the spinners
        durationSpinner.onItemSelectedListener = this
        genderSpinner.onItemSelectedListener = this

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

}
