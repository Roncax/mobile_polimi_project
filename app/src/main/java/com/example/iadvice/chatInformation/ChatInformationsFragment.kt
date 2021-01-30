package com.example.iadvice.chatInformation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.example.iadvice.databinding.ChatInformationBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.chat_information.view.*
import kotlinx.android.synthetic.main.fragment_new_question_images.view.*
import kotlinx.android.synthetic.main.other_question_chat.view.*

const val TAG = "CHAT_INFO_FRAGMENT"
class ChatInformationsFragment : Fragment() {

    private lateinit var binding: ChatInformationBinding
    private lateinit var viewModel: ChatInformationsViewModel
    var list_info: GridView? = null
    var adapter: InformationAdapter? = null

    private val chatListObserver = Observer<MutableList<StorageReference>> { imgList ->
        Log.d(
            TAG,
            "AdapterInfoList fired with my image list information: '${imgList}' "
        )
        attachInformationAdapter()
        binding.titleInfoQuestion.text = "AAAAAAaAAAAAAAAAAAAAAA"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.VISIBLE

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.chat_information,
            container,
            false
        )


        attachInformationAdapter()

        return inflater.inflate(R.layout.chat_information, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatInformationsViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PersistenceUtils.retrieveChatImages()
        viewModel = ViewModelProvider(this).get(ChatInformationsViewModel::class.java)
        PersistenceUtils.currentChatImagesLiveData.observe(this, chatListObserver)

    }


    fun attachInformationAdapter(){
        Log.d(TAG, "Adapter ${PersistenceUtils.currentChatImages}")
        list_info = requireActivity().findViewById(R.id.grindView_information)
        adapter = this.activity?.let { InformationAdapter(PersistenceUtils.currentChatImages, it) }
        list_info?.adapter = adapter
    }

}