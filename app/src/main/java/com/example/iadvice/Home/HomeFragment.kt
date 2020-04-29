package com.example.iadvice.Home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.iadvice.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI







class HomeFragment : Fragment() {

    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

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

    }


    /**
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called [.setHasOptionsMenu].  See
     * [Activity.onCreateOptionsMenu]
     * for more information.
     *
     * @param menu The options menu in which you place your items.
     *
     * @see .setHasOptionsMenu
     *
     * @see .onPrepareOptionsMenu
     *
     * @see .onOptionsItemSelected
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.nav_menu,menu)
    }


    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     *
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     *
     * @see .onCreateOptionsMenu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,requireView()!!.findNavController())
                ||super.onOptionsItemSelected(item)
    }

}




//TODO TabLayoutMediator
// - onPageChanceCallback --> adjust tab when viewpager2 change
// - InTabSelectedListener --> adjust viewpager2 when tab moves (useful?)
// - AdapterDataObserver --> recreate tab content when dataset change