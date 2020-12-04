package com.example.iadvice.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.chat.ChatActivity
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.YourQuestionsFragmentBinding


class RecyclerViewHandlerFragment() : Fragment(), OnItemClickListener {

    private val TAG = "RECYCLERVIEWHANDLER_FRAGMENT"

    private lateinit var binding: YourQuestionsFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private lateinit var viewModel: HomeFragmentViewModel

    private val chatListObserver = Observer<MutableList<Chat>> { _ ->
        Log.d(
            HomeFragment.TAG,
            "AdapterList fired with my ARCHIVED chats: '${viewModel.archivedChatList}' "
        )
        attachAdapter()
    }

    //private lateinit var chatType: String
      private var chatType= "archived"


    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(TAG,"onCreate")
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        Log.d(TAG,"chiamo viewModel.fetch")
        viewModel.fetchList()
        viewModel.archivedChatListLiveData.observe(this, chatListObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

/*
        val safeArgs: YourQuestionsFragmentArgs by navArgs()
        chatType = safeArgs.type
*/

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.your_questions_fragment,
            container,
            false
        )

        attachAdapter()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel.fetchList()
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!chatType.equals("your"))
            binding.fab.visibility = View.GONE
        else
            binding.fab.setOnClickListener { onFabClick() }

    }


    private fun attachAdapter() {
        var chatList: MutableList<Chat> = mutableListOf()
        when(chatType){
            "your" -> chatList = viewModel.myChatList
            "other" -> chatList = viewModel.otherChatList
            else -> chatList = viewModel.archivedChatList
        }

        viewAdapter = QuestionsAdapter(chatList, this@RecyclerViewHandlerFragment)

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

    private fun onFabClick() {
        findNavController().navigate(R.id.newQuestionFragment, null)
    }

    companion object {
        fun newInstance(chatType: String): RecyclerViewHandlerFragment {
            val fragment = RecyclerViewHandlerFragment()
            fragment.chatType = chatType
            Log.d("TAG","${chatType}")
            return fragment
        }
    }


}
