package com.example.iadvice.Home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.viewpager2.widget.ViewPager2
import com.example.iadvice.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import androidx.navigation.findNavController

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI


class HomeFragment : Fragment() {

    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var appBarConfiguration : AppBarConfiguration



    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout : View
        //Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.home_fragment, container, false)

        setHasOptionsMenu(true)
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

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * [.setRetainInstance] to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after [.onCreateView]
     * and before [.onViewStateRestored].
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity()!!.findViewById<AppBarLayout>(R.id.appBarLayout).setVisibility(View.VISIBLE)


/**
        /* Manage the creation of the toolbar (the actionbar) */
        toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        /* Manage the drawerMenu on the left */
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        navView = requireActivity().findViewById(R.id.navView)
        val toggle = ActionBarDrawerToggle(
            requireActivity(), drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
      //  navView.setNavigationItemSelectedListener(requireActivity())
*/



val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))



        toolbar = requireActivity().findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        drawerLayout = requireActivity().findViewById(R.id.drawer_layout)

        navView = requireActivity().findViewById(R.id.navView)

        val toggle = ActionBarDrawerToggle( requireActivity(), drawerLayout, toolbar, 0, 0)
        toggle.syncState()
        val navController = requireActivity().findNavController(R.id.myNavHostFragment)

        NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity,navController,drawerLayout)

        NavigationUI.setupWithNavController(navView, navController)




    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.nav_menu,menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,requireView()!!.findNavController())
                ||super.onOptionsItemSelected(item)
    }


}


//TODO TabLayoutMediator
// - onPageChanceCallback --> adjust tab when viewpager2 change
// - InTabSelectedListener --> adjust viewpager2 when tab moves (useful?)
// - AdapterDataObserver --> recreate tab content when dataset change