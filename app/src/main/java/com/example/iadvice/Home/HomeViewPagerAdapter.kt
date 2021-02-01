package com.example.iadvice.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.iadvice.database.Chat


class HomeViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    val TAG = "HOME_VIEWPAGER_ADAPTER"

    private val numOfPages: Int = 2


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecyclerViewHandlerFragment.newInstance( "your")
            else -> RecyclerViewHandlerFragment.newInstance("other")
        }
    }



    override fun getItemCount(): Int = numOfPages

}