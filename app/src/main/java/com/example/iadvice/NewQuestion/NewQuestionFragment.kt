package com.example.iadvice.newQuestion

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.databinding.NewQuestionFragmentBinding
import java.util.*

private const val TAG = "NewQuestionFragment"

class NewQuestionFragment : Fragment() {

    private lateinit var binding: NewQuestionFragmentBinding
    private lateinit var viewModel: NewQuestionViewModel
    val REQUEST_CODE = 200


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

        val c = Calendar.getInstance()

        binding.datePicker.minDate = c.timeInMillis
        c.add(Calendar.DAY_OF_MONTH, 30);
        binding.datePicker.maxDate = c.timeInMillis

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


            coverImageView.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
            }

            selectImagesNewchatButton.setOnClickListener{
                openGalleryForImages()
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

            viewModel.coverImage = imageUri!!
        }



        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData!!.itemCount

                for (i in 0..count - 1) {
                    imageUri= data.clipData!!.getItemAt(i).uri
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews

                    var newView: ImageView

                    newView = ImageView(this.context)
                    binding.imagesTable.addView(newView)

                    GlideApp.with(requireContext())
                        .load(imageUri)
                        .circleCrop()
                        .into(newView)
                    newView.layoutParams.height = 200
                    newView.layoutParams.width = 200
                    viewModel.coverImage = imageUri!!


                }

            } else if (data?.getData() != null) {
                // if single image is selected

                imageUri = data.data
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

            }
            viewModel.images.add(imageUri)
        }
    }


    private fun onCreateNewQuestion() {
        val p = binding.datePicker
        viewModel.expiration = getDate(p.year,p.month,p.dayOfMonth)!!
        viewModel.region = binding.regionSpinner.selectedItem.toString()
        viewModel.sex = binding.genderSpinner.selectedItem.toString()
        viewModel.title = binding.titleTexbox.text.toString()
        viewModel.categories = binding.categorySpinner.selectedItem.toString()
        viewModel.onCreateNewQuestion()
        requireView().findNavController()
            .navigate(R.id.action_newQuestionFragment_to_homeFragment)
    }


    private fun openGalleryForImages() {
        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures")
                , REQUEST_CODE
            )
        }
        else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    fun getDate(year: Int, month: Int, day: Int): Date? {
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
}
