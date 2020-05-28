package com.example.iadvice.Home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.iadvice.App
import com.example.iadvice.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid


        val layout: View
        //Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.home_fragment, container, false)
        return layout
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity()!!.findViewById<AppBarLayout>(R.id.appBarLayout)
            .setVisibility(View.VISIBLE)
    }
}
