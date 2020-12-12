package com.example.iadvice.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.YourQuestionsFragmentBinding


class YourQuestionsFragment() : Fragment(), OnItemClickListener {

    private lateinit var binding: YourQuestionsFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    var chatList: MutableList<Chat> = mutableListOf()

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener { onFabClick() }

    }


    private fun attachAdapter() {
        viewAdapter = QuestionsAdapter(chatList, this@YourQuestionsFragment)

        recyclerView = binding.RecyclerView.apply {
            //used to improve performances
            setHasFixedSize(true)
            adapter = viewAdapter
        }
    }


    override fun onItemClick(item: Chat) {
        //val intent = Intent(activity, ChatActivity::class.java)
        //intent.putExtra("chatId",item.chatId)
        //startActivity(intent)
        //TODO porcata, non utilizzare singleton per il passaggio dell'id della chat
        PersistenceUtils.currenChatId = item.chatId
        this.findNavController().navigate(R.id.action_homeFragment_to_chatActivityFragment)
    }

    private fun onFabClick() {
        findNavController().navigate(R.id.newQuestionFragment, null)
    }

    companion object {
        fun newInstance(chatList: MutableList<Chat>): YourQuestionsFragment {
            val fragment = YourQuestionsFragment()
            fragment.chatList = chatList
            Log.d("TAG", "${chatList}")
            return fragment
        }

        val TAG = "YOUR_QUESTION_FRAGMENT"

    }


}
