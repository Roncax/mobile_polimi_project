package com.example.iadvice.chatInformation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iadvice.R

class ChatInformations : Fragment() {

    companion object {
        fun newInstance() = ChatInformations()
    }

    private lateinit var viewModel: ChatInformationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_information, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatInformationsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}