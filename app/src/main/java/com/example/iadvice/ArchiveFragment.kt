package com.example.iadvice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.chat.ChatActivity
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.ArchiveFragmentBinding
import com.example.iadvice.home.HomeFragmentViewModel
import com.example.iadvice.home.OnItemClickListener
import com.example.iadvice.home.QuestionsAdapter
import androidx.lifecycle.Observer
import com.example.iadvice.home.HomeFragment


import java.util.*

const val TAG = "ARCHIVE_FRAGMENT"

class ArchiveFragment() : Fragment(), OnItemClickListener {

    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var binding: ArchiveFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private val chatListObserver = Observer<MutableList<Chat>> { _ ->
        Log.d(
            HomeFragment.TAG,
            "AdapterList fired with my ARCHIVED chats: '${viewModel.archivedChatList}' "
        )
        attachAdapter()
    }

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

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.archive_fragment,
            container,
            false
        )

        //todo vedere se si può togliere attachAdapter()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel.fetchList()
    }



    /**
     * Called when a fragment is first attached to its context.
     * [.onCreate] will be called after this.

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)

        Log.d("ARCHIVIO_ON_ATTACH","")
        viewModel.fetchList()
        Log.d("ARCHIVIO","ARCHIVIATE  '${viewModel.archivedChatList}'")
        viewModel.archivedChatListLiveData.observe(this, chatListObserver)
    }
*/





    private fun attachAdapter() {
        viewAdapter = QuestionsAdapter(viewModel.archivedChatList, this@ArchiveFragment)

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

    /*
    companion object {
        fun newInstance(chatList: MutableList<Chat>): ArchiveFragment {
            val fragment = ArchiveFragment()
            val TAG = "ARCHIVE_FRAGMENT"
            Log.d(TAG,"${chatList}")

            return fragment
        }
    }
    */
}
