package com.example.pictures_app

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.example.pictures_app.database.LocalDataSourceImplementation
import com.example.pictures_app.database.PicturesAppDatabase
import com.example.pictures_app.networking.NetworkStatusChecker
import com.example.pictures_app.networking.RemoteDataSourceImplementation
import com.example.pictures_app.networking.buildApiService
import com.example.pictures_app.repository.PicturesRepository
import com.example.pictures_app.repository.PicturesRepositoryImplementation

object ServiceLocator {

    private var database: PicturesAppDatabase? = null
    @Volatile
    var picturesRepository: PicturesRepository? = null

    fun providePicturesRepository(context: Context): PicturesRepository {
        synchronized(this) {
            return picturesRepository ?: createPicturesRepository(context)
        }
    }

    private fun createPicturesRepository(context: Context): PicturesRepository {
        val newPicturesRepository = PicturesRepositoryImplementation(
            createLocalDataSource(context),
            createRemoteDataSource(),
            buildNetworkStatusChecker(context)
        )
        picturesRepository = newPicturesRepository
        return newPicturesRepository
    }

    private fun createLocalDataSource(context: Context): LocalDataSourceImplementation {
        val database = database ?: createDataBase(context)
        return LocalDataSourceImplementation (
            picturesDao = database.picturesDao(),
            albumsDao = database.albumsDao(),
            postsDao = database.postsDao()
        )
    }

    private fun createDataBase(context: Context): PicturesAppDatabase {
        val result = PicturesAppDatabase.buildDatabase(context.applicationContext)
        database = result
        return result
    }

    private fun createRemoteDataSource(): RemoteDataSourceImplementation =
        RemoteDataSourceImplementation(buildApiService())

    private fun buildNetworkStatusChecker(context: Context): NetworkStatusChecker =
        NetworkStatusChecker(context.getSystemService(ConnectivityManager::class.java))

}