package com.example.iadvice.newQuestion

import android.app.Activity.RESULT_OK
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

private const val TAG = "NewQuestionFragment"

class NewQuestionFragment : Fragment() {

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
        val viewModelFactory = NewQuestionViewModelFactory(application)
        // viewModelProviders used to not destroy the viewmodel until detached
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewQuestionViewModel::class.java)


        binding.apply {
            createButton.setOnClickListener {
                if (titleTexbox.text.toString().isNotEmpty()) {
                    onCreateNewQuestion()
                } else {
                    Toast.makeText(
                        context, "You forgot to insert the title, please fill and retry",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }


            selectImagesNewchatButton.setOnClickListener {
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
        var imageUri: Uri? = null

        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            GlideApp.with(requireContext())
                .load(imageUri)
                .circleCrop()
                .into(binding.coverImageView)
        }

        viewModel.images.add(imageUri)
    }


    private fun onCreateNewQuestion() {
        viewModel.expiration = binding.expirationSpinner.selectedItem.toString()
        viewModel.region = binding.regionSpinner.selectedItem.toString()
        viewModel.sex = binding.genderSpinner.selectedItem.toString()
        viewModel.title = binding.titleTexbox.text.toString()
        viewModel.categories = binding.categorySpinner.selectedItem.toString()
        viewModel.onCreateNewQuestion()
        requireView().findNavController()
            .navigate(R.id.action_newQuestionFragment_to_homeFragment)
    }
}
