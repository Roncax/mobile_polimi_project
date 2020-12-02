package com.example.iadvice.chatInformation

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.navigation.fragment.findNavController
import com.example.iadvice.R

class ChatInformationsFragment : Fragment() {

    companion object {
        fun newInstance() = ChatInformationsFragment()
    }

    private lateinit var viewModel: ChatInformationsViewModel
    var list_info: GridView? = null
    var informationList: ArrayList<Uri> = ArrayList<Uri>()
    var adapter: InformationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        list_info = activity?.findViewById<GridView>(R.id.grindView_information)
        adapter = InformationAdapter(informationList, requireActivity())
        list_info!!.adapter = adapter


        return inflater.inflate(R.layout.chat_information, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatInformationsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}