package com.example.iadvice

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.iadvice.database.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MAIN_ACTIVITY"
    }

    private val userObserver = Observer<User> { user ->
        navView.getHeaderView(0).findViewById<TextView>(R.id.navUsername).text = user.username
        navView.getHeaderView(0).findViewById<TextView>(R.id.navSubtitle).text =
            "${user.points} owned points"
    }

    private val userImageObserver = Observer<StorageReference> { imageRef ->
        val imageView = navView.getHeaderView(0).findViewById<ImageView>(R.id.navImage)
        GlideApp.with(this)
            .load(imageRef)
            .circleCrop()
            .into(imageView)

    }

    // Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment?
                ?: return
        // Set up Action Bar
        val navController = host.navController
        //setOf() contains the top-level destinations of out application
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment), drawerLayout)
        setupActionBar(navController, appBarConfiguration)

        navView = findViewById(R.id.nav_View)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { item ->
            drawerListener(item)
        }

        PersistenceUtils.currentUserLiveData.observe(this, userObserver)
        PersistenceUtils.currentUserImageLiveData.observe(this, userImageObserver)




    }

    private fun setupActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        // This allows NavigationUI to decide what label to show in the action bar
        // By using appBarConfig, it will also determine whether to
        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, appBarConfig)
    }


    //todo mettere mano qui(e setupNavigationMenu) per layout diverso su schermi grandi
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshButton -> {
                Toast.makeText(
                    this, "Chat list refreshed",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController(R.id.myNavHostFragment).popBackStack()
            }

            else -> item.onNavDestinationSelected(findNavController(R.id.myNavHostFragment))
                    || super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.myNavHostFragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun drawerListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOutFragment -> {
                FirebaseAuth.getInstance().signOut()
                findNavController(R.id.myNavHostFragment).popBackStack()
                Log.d(TAG, "On logOut selected in drawer")
            }
            R.id.settingsFragment -> {
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeFragment_to_settingsFragment)
            }
            R.id.archiveFragment ->{
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeFragment_to_your_questions_fragment)
            }
            R.id.rankingFragment ->{
                findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeFragment_to_rankingFragment)
            }



        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}