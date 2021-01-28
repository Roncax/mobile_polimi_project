package com.example.iadvice.home

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.iadvice.MainActivity
import com.example.iadvice.R
import com.example.iadvice.database.Chat
import com.example.iadvice.databinding.HomeFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    companion object {
        const val TAG = "HOME_FRAGMENT"
    }

    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    private lateinit var viewPager: ViewPager2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        displayHomeChats()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<HomeFragmentBinding>(
            inflater,
            R.layout.home_fragment, container, false
        )


        //The appbar become VISIBLE
        requireActivity().findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.VISIBLE

        //Force the screen orientation
        //todo da vedere activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR


        return binding.root
    }


    private fun displayHomeChats() {
        homeViewPagerAdapter =
            HomeViewPagerAdapter(this@HomeFragment)
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