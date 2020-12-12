package com.example.iadvice.newQuestion

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.GlideApp
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.NewQuestionFragmentBinding
import androidx.lifecycle.Observer
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.chatInformation.ChatInformationsFragment
import com.example.iadvice.chatInformation.InformationAdapter
import java.util.*

private const val TAG = "NEWQUESTION_FRAGMENT"

class NewQuestionFragment : Fragment() {

    private lateinit var binding: NewQuestionFragmentBinding
    private lateinit var viewModel: NewQuestionViewModel
    private val REQUEST_CODE = 200

    private val newChatObserver = Observer<Chat>{ _ ->
        popStack()
    }

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
        viewModel.newChatLiveData.observe(viewLifecycleOwner, newChatObserver)

        val c = Calendar.getInstance()

        c.add(Calendar.DAY_OF_MONTH, 1);
        binding.datePicker.minDate = c.timeInMillis
        c.add(Calendar.DAY_OF_MONTH, 360);
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
                }
            }


            coverImageView.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
            }

            selectImagesNewchatButton.setOnClickListener {
                openGalleryForImages()
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


        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            viewModel.images = mutableListOf()
            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData!!.itemCount
                for (i in 0..count - 1) {
                    imageUri = data.clipData!!.getItemAt(i).uri
                    viewModel.images.add(imageUri)


                    Log.d(TAG, "Images $imageUri in")
                    Log.d(TAG, "Images viewmodel ${viewModel.images} in")

                }
                attachGridNewChatAdapter()
            }
        }
    }


    private fun onCreateNewQuestion() {
        val p = binding.datePicker
        viewModel.expiration = getDate(p.year, p.month, p.dayOfMonth)!!
        viewModel.region = binding.regionSpinner.selectedItem.toString()
        viewModel.sex = binding.genderSpinner.selectedItem.toString()
        viewModel.title = binding.titleTexbox.text.toString()
        viewModel.category = binding.categorySpinner.selectedItem.toString()
        viewModel.onCreateNewQuestion()
        Log.d(TAG, "onCreateNewQuestion")
    }

    fun popStack(){
        if (!requireView().findNavController().popBackStack()) {
            Toast.makeText(
                context, "Cannot go back to the chats, error!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun openGalleryForImages() {
        // For latest versions API LEVEL 19+
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE);
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


    fun attachGridNewChatAdapter(){
        Log.d(TAG, "Adapter")
        var list_info = requireActivity().findViewById<GridView>(R.id.image_grindView)
        var adapter = this.activity?.let { newChatGridAdapter(viewModel.images, it) }
        list_info?.adapter = adapter
    }

}
