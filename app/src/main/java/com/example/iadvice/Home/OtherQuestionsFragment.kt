package com.example.iadvice.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iadvice.R


class OtherQuestionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.other_questions_fragment, container, false)
    }


}
*/
package com.example.iadvice.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.R
import com.example.iadvice.chat.ChatActivity
import com.example.iadvice.chat.ChatActivityArgs
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.OtherQuestionsFragmentBinding


class OtherQuestionsFragment(var chatList: MutableList<Chat>) : Fragment(), OnItemClickListener {

    private lateinit var binding: OtherQuestionsFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private lateinit var userId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.other_questions_fragment,
            container,
            false
        )

        attachAdapter()

        return binding.root
    }

    private fun attachAdapter() {

        viewAdapter = QuestionsAdapter(chatList, this@OtherQuestionsFragment)

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

}

