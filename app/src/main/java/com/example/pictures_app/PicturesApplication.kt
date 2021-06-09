package com.example.pictures_app

import android.app.Application
import android.net.ConnectivityManager
import com.example.pictures_app.database.PicturesAppDatabase
import com.example.pictures_app.networking.RemoteApi
import com.example.pictures_app.networking.RemoteApiService
import com.example.pictures_app.networking.buildApiService
import com.example.pictures_app.repository.PicturesRepository
import com.example.pictures_app.repository.PicturesRepositoryImplementation

class PicturesApplication : Application() {

    companion object {
        private lateinit var instance: PicturesApplication
        private lateinit var apiService: RemoteApiService
        lateinit var remoteApi:RemoteApi
        private lateinit var database: PicturesAppDatabase
        lateinit var picturesRepository: PicturesRepository
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        apiService = buildApiService()
        remoteApi = RemoteApi(apiService)

        database = PicturesAppDatabase.buildDatabase(instance)
        picturesRepository = PicturesRepositoryImplementation(
            database.picturesDao(),
            instance
        )
    }
}