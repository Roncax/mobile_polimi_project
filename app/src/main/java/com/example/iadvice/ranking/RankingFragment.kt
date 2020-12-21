package com.example.iadvice.ranking

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.PersistenceUtils
import com.example.iadvice.R
import com.example.iadvice.chat.ChatActivityFragment
import com.example.iadvice.chat.MessageAdapter
import com.example.iadvice.database.Chat
import com.example.iadvice.database.Message
import com.example.iadvice.database.User
import com.example.iadvice.databinding.RankingFragmentBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RankingFragment : Fragment() {

    companion object{
        const val TAG = "RANKING_FRAGMENT"
    }

    private lateinit var binding: RankingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // bind the login_fragment layout with the binding variable
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.ranking_fragment, container, false
        )

        binding.rankingList.layoutManager = LinearLayoutManager(context)
        val adapter = CustomAdapter(requireContext(), PersistenceUtils.userListRank)
        binding.rankingList.adapter = adapter

        return binding.root
    }

}