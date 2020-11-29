package com.example.iadvice.ranking

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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


    private lateinit var viewModel: RankingViewModel
    private lateinit var adapter: MessageAdapter
    private lateinit var binding: RankingFragmentBinding

    lateinit var userListRank: MutableList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // bind the login_fragment layout with the binding variable
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.ranking_fragment, container, false
        )

        retrieveSortedUserList()




        return binding.root
    }

    private fun retrieveSortedUserList() {
        userListRank = mutableListOf()

        val onlineDb = Firebase.database.reference
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{u ->
                    val user: User? = u.getValue(User::class.java)
                    userListRank.add(user!!)
                }
                val adapter = ListAdapter(userListRank)
                binding.rankingList.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }
        onlineDb.child("users").addListenerForSingleValueEvent(userListener)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RankingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}