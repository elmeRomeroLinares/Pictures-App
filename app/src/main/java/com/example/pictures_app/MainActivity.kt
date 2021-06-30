package com.example.pictures_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.pictures_app.databinding.ActivityMainBinding
import com.example.pictures_app.utils.ActionBarTitleSetter
import com.example.pictures_app.utils.Notifier
import com.example.pictures_app.utils.toast
import com.google.android.material.navigation.NavigationView
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), ActionBarTitleSetter {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var _binding: ActivityMainBinding? = null
    private val activityMainBinding get() = _binding!!

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityViewModel = ViewModelProvider(this)
            .get(MainActivityViewModel::class.java)

        openFromFirebaseDynamicLink()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setSupportActionBar(activityMainBinding.appBarMain.toolbar)

        Notifier.init(this)

        initUi()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        openFromFirebaseDynamicLink()
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

    private fun openFromFirebaseDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                if (pendingDynamicLinkData != null) {
                    if (mainActivityViewModel.dynamicLinkData.value != pendingDynamicLinkData.link) {
                        navigateToDeepLinkDestination(pendingDynamicLinkData.link)
                        mainActivityViewModel.dynamicLinkData.postValue(pendingDynamicLinkData.link)
                    }
                }
            }
            .addOnFailureListener(this) {this.toast(getString(R.string.error_message))}
    }

    private fun navigateToDeepLinkDestination(deepLinkUri: Uri?) {
        val lastPathSegment = deepLinkUri?.lastPathSegment
        val queryParameters = deepLinkUri?.getQueryParameter(KEY_ID)
        val bundle = bundleOf(ELEMENT_ID to queryParameters)
        if (lastPathSegment == POST_KEY) {
            navController.navigate(
                R.id.postDetailFragment,
                bundle
            )
        } else if (lastPathSegment == PHOTOS_KEY) {
            navController.navigate(
                R.id.imageDetailFragment,
                bundle
            )
        }
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    companion object {
        const val ELEMENT_ID = "elementId"
        const val KEY_ID = "id"
        const val POST_KEY = "posts"
        const val PHOTOS_KEY = "photos"
    }
}