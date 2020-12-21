package com.example.iadvice.home

import android.content.Context
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
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.YourQuestionsFragmentBinding

const val KEY_CHATTYPE = "type_of_chat_to_display"

class RecyclerViewHandlerFragment() : Fragment(), OnItemClickListener {

    private val TAG = "RECYCLERVIEWHANDLER_FRAGMENT"

    private lateinit var binding: YourQuestionsFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private lateinit var viewModel: HomeFragmentViewModel

    private val chatListObserver = Observer<MutableList<Chat>> { chat ->
        Log.d(
            HomeFragment.TAG,
            "AdapterList fired with my chats: '${chat}' "
        )
        attachAdapter()
    }

    //private lateinit var chatType: String
      private var chatType= "archived"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState != null){
            chatType = savedInstanceState.getString(KEY_CHATTYPE,"archived")
        }

        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        Log.d(TAG,"chiamo viewModel.fetch")
        viewModel.fetchList()

        viewModel.archivedChatListLiveData.observe(this, chatListObserver)
        viewModel.myChatListLiveData.observe(this, chatListObserver)
        viewModel.otherChatListLiveData.observe(this, chatListObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        Log.d(TAG, "onActivityCreated called")
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

        viewAdapter = QuestionsAdapter(chatList, chatType, this@RecyclerViewHandlerFragment)

        recyclerView = binding.RecyclerView.apply {
            //used to improve performances
            setHasFixedSize(true)
            adapter = viewAdapter
        }
    }


    override fun onItemClick(item: Chat) {
        PersistenceUtils.currenChatId = item.chatId
        if(chatType == "archived"){findNavController().navigate(R.id.action_your_questions_fragment_to_chatActivityFragment)}
        else{findNavController().navigate(R.id.action_homeFragment_to_chatActivityFragment)}

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


    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to [.onCreate],
     * [.onCreateView], and
     * [.onActivityCreated].
     *
     *
     * This corresponds to [ Activity.onSaveInstanceState(Bundle)][Activity.onSaveInstanceState] and most of the discussion there
     * applies here as well.  Note however: *this method may be called
     * at any time before [.onDestroy]*.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState!!.putString(KEY_CHATTYPE,chatType)
    }
}
