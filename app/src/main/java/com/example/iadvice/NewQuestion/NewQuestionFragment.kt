package com.example.iadvice.newQuestion

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.databinding.NewQuestionFragmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

private const val TAG = "NEWQUESTION_FRAGMENT"

class NewQuestionFragment : Fragment() {

    private lateinit var binding: NewQuestionFragmentBinding
    private lateinit var viewModel: NewQuestionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.new_question_fragment,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = NewQuestionViewModelFactory(application)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(NewQuestionViewModel::class.java)

        binding.apply {

            val c = Calendar.getInstance()

            c.add(Calendar.DAY_OF_MONTH, 1);
            datePicker.minDate = c.timeInMillis
            c.add(Calendar.DAY_OF_MONTH, 360);
            datePicker.maxDate = c.timeInMillis


            nextButton.setOnClickListener {
                if (titleTexbox.text.toString()
                        .isNotEmpty() and questionEditTextView.text.toString().isNotEmpty()
                ) {
                    fillViewModel()
                    requireView().findNavController()
                        .navigate(R.id.action_newQuestionFragment_to_newQuestionImagesFragment)
                } else {
                    Toast.makeText(
                        context,
                        "You forgot to insert the title or the question, please fill and retry",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            coverImageView.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
            }

        }
        return binding.root

    }

    //Manage the result in pick image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageUri: Uri?

        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            GlideApp.with(requireContext())
                .load(imageUri)
                .circleCrop()
                .into(binding.coverImageView)

            viewModel.coverImage = imageUri!!
        }
    }


    private fun getDate(year: Int, month: Int, day: Int): Date? {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.time
    }

    private fun fillViewModel() {
        val p = binding.datePicker
        viewModel.expiration = getDate(p.year, p.month, p.dayOfMonth)!!
        viewModel.region = binding.regionSpinner.selectedItem.toString()
        viewModel.sex = binding.genderSpinner.selectedItem.toString()
        viewModel.title = binding.titleTexbox.text.toString()
        viewModel.category = binding.categorySpinner.selectedItem.toString()
        viewModel.question = binding.questionEditTextView.text.toString()
        viewModel.maxUsers = binding.maxUserSlider.value.toInt()
    }


}
