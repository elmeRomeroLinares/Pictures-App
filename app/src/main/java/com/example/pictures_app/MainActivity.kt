package com.example.pictures_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import androidx.navigation.navOptions
import androidx.navigation.ui.*
import com.example.pictures_app.databinding.ActivityMainBinding
import com.example.pictures_app.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

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

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        openFromFirebaseDynamicLink()

        openFromFirebaseNotification()

        setSupportActionBar(activityMainBinding.appBarMain.toolbar)

        Notifier.init(this)

        getFirebaseNotificationToken()

        subscribeToFirebaseTopic()

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
        deepLinkUri?.let {
            navController.navigate(deepLinkUri)
        }
    }

    private fun openFromFirebaseNotification() {
        intent?.getStringExtra(DESTINATION)?.let {
            val destinationUri = Uri.parse(it)
            if (mainActivityViewModel.dynamicLinkData.value != destinationUri) {
                navigateToDeepLinkDestination(destinationUri)
                mainActivityViewModel.dynamicLinkData.postValue(destinationUri)
            }
        }
    }

    private fun getFirebaseNotificationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                showMessage(getString(R.string.error_message))
                return@OnCompleteListener
            }

            SharedPreferencesManager.persistFirebaseToken(task.result)
        })
    }

    private fun subscribeToFirebaseTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(DEVELOPMENT)
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun showMessage(message: String) {
        this.toast(message)
    }

    companion object {
        const val DEVELOPMENT = "dev"
        const val DESTINATION = "destination"
    }
}