package com.example.iadvice.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.HomeFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

const val TAG = "HOME_FRAGMENT"

class HomeFragment : Fragment() {

    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    private lateinit var viewPager: ViewPager2


    private lateinit var userId: String

    var myChatId: MutableList<String> = mutableListOf()
    var otherChatId: MutableList<String> = mutableListOf()
    var myChatList: MutableList<Chat> = mutableListOf()
    var otherChatList: MutableList<Chat> = mutableListOf()
    //TODO implementare l'archivio chat con questa lista
    var archivedChatList: MutableList<Chat> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "In onCreate")
        userId = FirebaseAuth.getInstance().uid!!

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //initialization of the lists
        myChatId = mutableListOf()
        otherChatId = mutableListOf()
        myChatList = mutableListOf()
        otherChatList = mutableListOf()

        Log.d(TAG, "Binding in home fragment")
        val binding = DataBindingUtil.inflate<HomeFragmentBinding>(
            inflater,
            R.layout.home_fragment, container, false
        )

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.VISIBLE
        findChatsId()

    }

    private fun findChatsId() {

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("chatlist")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.child("your").children) {
                        myChatId.add(snapshot.value.toString())
                        Log.d(
                            TAG,
                            "Messaggio recuperato in findChatsId - my chats:${snapshot.value.toString()}"
                        )
                    }
                    for (snapshot in dataSnapshot.child("other").children) {
                        otherChatId.add(snapshot.value.toString())
                        Log.d(
                            TAG,
                            "Messaggio recuperato in findChatsId - other chats:${snapshot.value.toString()}"
                        )
                    }
                    retrieveChatsFromId()
                }
            })


    }


    private fun retrieveChatsFromId() {
        val mDatabase = FirebaseDatabase.getInstance().reference
        FirebaseDatabase.getInstance().reference
            .child("chats")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    //TODO To implement
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (chatName in myChatId) {

                        val chat: Chat? = dataSnapshot.child(chatName).getValue(Chat::class.java)
                        if (chat?.expiration!!.before(Calendar.getInstance().time)){
                            FirebaseDatabase.getInstance().reference.child("chats").child(chatName).setValue(chat)
                            continue
                        }
                        if (chat.isActive) {
                            myChatList.add(chat)
                        } else {
                            archivedChatList.add(chat)
                        }
                    }
                    for (chatName in otherChatId) {
                        val chat: Chat? = dataSnapshot.child(chatName).getValue(Chat::class.java)
                        if (chat?.expiration!!.before(Calendar.getInstance().time)){
                            chat.isActive = false
                            FirebaseDatabase.getInstance().reference.child("chats").child(chatName).setValue(chat)
                            continue
                        }
                        if (chat.isActive) {
                            otherChatList.add(chat)
                            Log.d(TAG,"OTHER --> ${otherChatList}")
                        } else {
                            archivedChatList.add(chat)
                            Log.d(TAG,"SCADUTE --> ${archivedChatList}")
                        }
                    }
                    displayHomeChats()
                }

            })

    }


    //TODO qua si stanno creando le tab quando vengon tirate giú le chat. Le tab devono essere giá presenti!
    private fun displayHomeChats() {
        Log.d(TAG, "Chats in Home uploading...")
        homeViewPagerAdapter = HomeViewPagerAdapter(this@HomeFragment, myChatList, otherChatList)
        viewPager = requireView().findViewById(R.id.pager)
        viewPager.adapter = homeViewPagerAdapter

        val tabLayout = requireView().findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "My chats"
                else -> tab.text = "From the world"
            }
        }.attach()
    }
}