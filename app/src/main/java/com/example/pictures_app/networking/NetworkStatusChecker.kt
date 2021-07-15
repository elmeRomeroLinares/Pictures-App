package com.example.pictures_app.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStatusChecker @Inject constructor(
    @ApplicationContext context: Context
) : NetworkStatusCheckerInterface {

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    override fun hasInternetConnection() : Boolean {
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities  = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}
