package com.example.pictures_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.pictures_app.databinding.ActivityMainBinding
import com.example.pictures_app.utils.ActionBarTitleSetter
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), ActionBarTitleSetter {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.appBarMain.toolbar)
        initUi()
    }

    private fun initUi() {
        val drawerLayout: DrawerLayout = activityMainBinding.drawerLayout
        val navView: NavigationView = activityMainBinding.navView
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setAppBarConfiguration(drawerLayout, navView)
    }

    private fun setAppBarConfiguration(
        drawerLayout: DrawerLayout,
        navView: NavigationView
    ) {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.postsListFragment, R.id.albumsViewPagerFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }
}