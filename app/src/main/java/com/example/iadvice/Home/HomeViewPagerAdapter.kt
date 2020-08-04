package com.example.iadvice.Home

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.iadvice.database.Chat

class HomeViewPagerAdapter(fragment: Fragment, yourChats: MutableList<Chat>, otherChats: MutableList<Chat> ) : FragmentStateAdapter(fragment) {

    private val numOfPages: Int = 2
    var yourChats:MutableList<Chat> = yourChats
    var otherChats:MutableList<Chat> = otherChats

    /**
     * Provide a new Fragment associated with the specified position.
     *
     *
     * The adapter will be responsible for the Fragment lifecycle:
     *
     *  * The Fragment will be used to display an item.
     *  * The Fragment will be destroyed when it gets too far from the viewport, and its state
     * will be saved. When the item is close to the viewport again, a new Fragment will be
     * requested, and a previously saved state will be used to initialize it.
     *
     * @see ViewPager2.setOffscreenPageLimit
     */
    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return YourQuestionsFragment(yourChats)
            else -> return OtherQuestionsFragment(otherChats)
        }
    }

    override fun getItemCount(): Int = numOfPages

}
