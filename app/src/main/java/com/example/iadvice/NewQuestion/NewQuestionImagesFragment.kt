package com.example.iadvice.newQuestion

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.FragmentNewQuestionImagesBinding

private const val TAG = "NEWQUESTIONIMG_FRAGMENT"

class NewQuestionImagesFragment : Fragment() {

    private lateinit var binding: FragmentNewQuestionImagesBinding
    private lateinit var viewModel: NewQuestionViewModel

    private val REQUEST_CODE = 200


    private val newChatObserver = Observer<Chat>{ _ ->
        popStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_question_images,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val viewModelFactory = NewQuestionViewModelFactory(application)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(NewQuestionViewModel::class.java)
        viewModel.newChatLiveData.observe(viewLifecycleOwner, newChatObserver)

        binding.titleNewChatQuestion.text = viewModel.question
        binding.selectImagesButton.setOnClickListener {
            openGalleryForImages()
        }

        binding.createButton.setOnClickListener {
            onCreateNewQuestion()
        }

        return binding.root
    }

    private fun onCreateNewQuestion() {
        viewModel.onCreateNewQuestion()
        Log.d(TAG, "onCreateNewQuestion")
    }

    fun attachGridNewChatAdapter() {
        Log.d(TAG, "Adapter")
        var list_info = requireActivity().findViewById<GridView>(R.id.grindView_newQuestion)
        var adapter = this.activity?.let { newChatGridAdapter(viewModel.images, it) }
        list_info?.adapter = adapter
    }

    private fun openGalleryForImages() {
        // For latest versions API LEVEL 19+
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE);
    }


    fun popStack() {
        if (!requireView().findNavController().popBackStack()) {
            Toast.makeText(
                context, "Cannot go back to the chats, error!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var imageUri: Uri?
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

}