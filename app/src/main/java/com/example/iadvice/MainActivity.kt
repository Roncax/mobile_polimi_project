package com.example.iadvice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.recyclerview.widget.RecyclerView
import com.example.iadvice.database.Chat
import com.example.iadvice.home.QuestionsAdapter
import com.example.iadvice.home.YourQuestionsFragment

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.lang.reflect.Array.newInstance
import javax.xml.datatype.DatatypeFactory.newInstance

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var archivedChatList: MutableList<Chat> = mutableListOf()
    private lateinit var archiveFragment:ArchiveFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment? ?: return
        // Set up Action Bar
        val navController = host.navController
        val drawerLayout : DrawerLayout? = findViewById(R.id.drawer_layout)
        //setOf() contains the top-level destinations of out application
        appBarConfiguration = AppBarConfiguration( setOf(R.id.homeFragment, R.id.loginFragment), drawerLayout)
        setupActionBar(navController, appBarConfiguration)
        setupNavigationMenu(navController)
    }

    private fun setupActionBar(navController: NavController, appBarConfig : AppBarConfiguration) {
        // This allows NavigationUI to decide what label to show in the action bar
        // By using appBarConfig, it will also determine whether to
        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.navView)
        sideNavView?.setupWithNavController(navController)
    }


    //todo mettere mano qui(e setupNavigationMenu) per layout diverso su schermi grandi
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOutFragment -> {
                FirebaseAuth.getInstance().signOut()
                findNavController(R.id.myNavHostFragment).navigate(R.id.loginFragment)
                true
            }
            R.id.archiveFragment -> {
                //val questionsAdapter = QuestionsAdapter.QuestionChatViewHolder.Companion

                findNavController(R.id.myNavHostFragment).navigate(R.id.archiveFragment)
                true
            }

            else -> item.onNavDestinationSelected(findNavController(R.id.myNavHostFragment))
                    || super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.myNavHostFragment).navigateUp(appBarConfiguration)
    }

    fun setArchivedChats( archivedChats: MutableList<Chat> ){
        this.archivedChatList = archivedChats
        Log.d("MainActivity", "ARCHIVE PASSED ${archivedChatList}")
        archiveFragment = ArchiveFragment.newInstance(archivedChatList)
    }

}