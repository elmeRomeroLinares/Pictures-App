package com.example.pictures_app.data.source

import com.example.pictures_app.networking.NetworkStatusCheckerInterface

class FakeNetworkStatusChecker(
    private val isNetworkConnected: Boolean
) : NetworkStatusCheckerInterface {

    override fun hasInternetConnection(): Boolean {
        return isNetworkConnected
    }

}