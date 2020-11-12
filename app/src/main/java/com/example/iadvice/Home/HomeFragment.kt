package com.example.iadvice.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    private lateinit var viewPager: ViewPager2


    private lateinit var userId: String
    var yourChatList: MutableList<Chat> = mutableListOf()
    var otherChatList: MutableList<Chat> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = FirebaseAuth.getInstance().currentUser!!.uid

        val layout: View
        //Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.home_fragment, container, false)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity()!!.findViewById<AppBarLayout>(R.id.appBarLayout)
            .setVisibility(View.VISIBLE)

        findYourChats()
        displayHomeChats()
    }

    private fun findYourChats() {
        var chatToRetrieve: MutableList<String> = mutableListOf()

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("chatlist")
            .child("your")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.getChildren()) {
                        val value = snapshot.value
                        chatToRetrieve.add(value.toString())
                    }
                    retrieveChats(chatToRetrieve)
                }
            })
    }

    private fun findOtherChats() {
        var chatToRetrieve: MutableList<String> = mutableListOf()

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("chatlist")
            .child("other")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.getChildren()) {
                        val value = snapshot.value
                        chatToRetrieve.add(value.toString())
                    }
                    retrieveOtherChats(chatToRetrieve)
                }
            })
    }


    private fun retrieveChats(chatToRetrieve: MutableList<String>) {
        for (chatName in chatToRetrieve) {
            FirebaseDatabase.getInstance().reference
                .child("chats")
                .child(chatName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {
                        //TODO To implement
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val chat: Chat? = dataSnapshot.getValue(Chat::class.java)
                        if (chat != null) {
                            yourChatList.add(chat)
                        }

                        findOtherChats()

                    }

                })

        }

    }

    private fun retrieveOtherChats(chatToRetrieve: MutableList<String>) {
        for (chatName in chatToRetrieve) {
            FirebaseDatabase.getInstance().reference
                .child("chats")
                .child(chatName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {
                        //TODO To implement
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val chat: Chat? = dataSnapshot.getValue(Chat::class.java)
                        if (chat != null) {
                            otherChatList.add(chat)
                        }

                        displayHomeChats()

                    }

                })

        }

    }


    //TODO qua si stanno creando le tab quando vengon tirate giú le chat. Le tab devono essere giá presenti!
    private fun displayHomeChats() {
        homeViewPagerAdapter = HomeViewPagerAdapter(this@HomeFragment, yourChatList, otherChatList)
        viewPager = requireView().findViewById(R.id.pager)
        viewPager.adapter = homeViewPagerAdapter

        val tabLayout = requireView().findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "My chats"
                else -> tab.text = "From the world"  //todo export the names of the tabs
            }
        }.attach()
    }
}