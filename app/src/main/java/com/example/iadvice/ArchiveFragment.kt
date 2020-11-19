package com.example.iadvice

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    /**
     * Called to do initial creation of a fragment.  This is called after
     * [.onAttach] and before
     * [.onCreateView].
     *
     *
     * Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see [.onActivityCreated].
     *
     *
     * Any restored child fragments will be created before the base
     * `Fragment.onCreate` method returns.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val TAG = "ARCHIVE_FRAGMENT"
            Log.d(TAG,"${chatList}")

            return fragment
        }
    }





}
