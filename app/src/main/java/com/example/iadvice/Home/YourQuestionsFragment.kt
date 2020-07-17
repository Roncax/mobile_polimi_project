package com.example.iadvice.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.YourQuestionsFragmentBinding
import com.google.firebase.auth.FirebaseAuth



class YourQuestionsFragment(chatList: MutableList<Chat>) : Fragment(), OnItemClickListener {

    private lateinit var binding: YourQuestionsFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    var chatList:MutableList<Chat> = chatList

    private lateinit var userId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userId = FirebaseAuth.getInstance().currentUser!!.uid

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.your_questions_fragment,
            container,
            false
        )

        attachAdapter()


        return binding.root
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener { onFabClick() }

    }



    private fun attachAdapter() {

        Log.d("YOURQUESTION","chatID del primo ${chatList[0].chatId}")
        viewAdapter = QuestionsAdapter(chatList, this@YourQuestionsFragment)

        recyclerView = binding.RecyclerView.apply {
            //used to improve performances
            setHasFixedSize(true)
            adapter = viewAdapter
        }
    }


    override fun onItemClick(item: Chat) {
        //Toast.makeText(activity, "${item.question} selected", Toast.LENGTH_SHORT).show()

/* TODO NAVIGATION
        val action = YourQuestionsFragmentDirections.actionYourQuestionsFragmentToChatActivity()
        findNavController().navigate(action)
        */

       /* Log.i("ACTION","${item.chatId}")
        val action = YourQuestionsFragmentDirections.actionYourQuestionsFragmentToChatActivity(123)
        findNavController().navigate(action)
        */

        Log.d("TOAST","dsadsasda ${item.chatId}")
        Toast.makeText(activity, "${item.chatId} selected", Toast.LENGTH_SHORT).show()

    }

    fun onFabClick() {
        findNavController().navigate(R.id.newQuestionFragment, null)
    }

}
