package com.example.iadvice.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.iadvice.R
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
    private lateinit var chatList: String

    /* Used for JSON mamagement in the innerClass */
    private var dataList = ArrayList<HashMap<String, String>>()





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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        findChats(object : FirebaseCallback {
            override fun onCallback(list: String) {
                Log.i("CICCIO","${chatList}")
                val cacca= ArrayList<String>()
                cacca.add(chatList)
                processData(cacca)
            }
        })



        homeViewPagerAdapter = HomeViewPagerAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = homeViewPagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "YOUR"
                else -> tab.text = "OTHER"  //todo export the names of the tabs
            }
        }.attach()
    }

    private fun findChats(firebaseCallback: FirebaseCallback) {

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("chatlist")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    chatList = dataSnapshot.value.toString()
                    Log.i("PORK-VALUES","${chatList}")
                    //processData()
                    firebaseCallback.onCallback(chatList)
                }
            })
    }


    private fun processData(chatList: ArrayList<String>) {

        for (i in 0 until chatList.size) {
            val map = HashMap<String, String>()
            map["title"] = chatList.get(i)
            map["info"] = "pirupiru"
            dataList.add(map)
        }
        Log.i("CACCCA","${dataList}")

    }

    private interface FirebaseCallback{
        fun onCallback(list: String)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity()!!.findViewById<AppBarLayout>(R.id.appBarLayout)
            .setVisibility(View.VISIBLE)
    }
}
