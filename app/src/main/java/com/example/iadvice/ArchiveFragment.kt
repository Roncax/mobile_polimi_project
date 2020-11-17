package com.example.iadvice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.chat.ChatActivity
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.ArchiveFragmentBinding
import com.example.iadvice.home.OnItemClickListener
import com.example.iadvice.home.QuestionsAdapter


class ArchiveFragment() : Fragment(), OnItemClickListener {

    private lateinit var binding: ArchiveFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    var chatList: MutableList<Chat> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.archive_fragment,
            container,
            false
        )

        attachAdapter()

        return binding.root
    }

    private fun attachAdapter() {
        viewAdapter = QuestionsAdapter(chatList, this@ArchiveFragment)

        recyclerView = binding.RecyclerView.apply {
            //used to improve performances
            setHasFixedSize(true)
            adapter = viewAdapter
        }

    }

    override fun onItemClick(item: Chat) {
        val intent = Intent(activity, ChatActivity::class.java)
        intent.putExtra("chatId",item.chatId)
        startActivity(intent)
    }

    companion object {
        fun newInstance(chatList: MutableList<Chat>): ArchiveFragment {
            val fragment = ArchiveFragment()
            fragment.chatList = chatList
            return fragment
        }
    }


}
