package com.example.pictures_app

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.pictures_app.database.LocalDataSourceImplementation
import com.example.pictures_app.database.PicturesAppDatabase
import com.example.pictures_app.networking.NetworkStatusChecker
import com.example.pictures_app.networking.RemoteDataSourceImplementation
import com.example.pictures_app.networking.buildApiService
import com.example.pictures_app.repository.PicturesRepository
import com.example.pictures_app.repository.PicturesRepositoryImplementation
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

object ServiceLocator {
    private val lock = Any()

    @Inject
    lateinit var localDataSourceImplementation: LocalDataSourceImplementation

    @Volatile
    var picturesRepository: PicturesRepository? = null @VisibleForTesting set

    fun providePicturesRepository(context: Context): PicturesRepository {
        synchronized(this) {
            return picturesRepository ?: createPicturesRepository(context)
        }
    }

    private fun createPicturesRepository(context: Context): PicturesRepository {
        val newPicturesRepository = PicturesRepositoryImplementation(
            localDataSourceImplementation,
            createRemoteDataSource(),
            buildNetworkStatusChecker(context)
        )
        picturesRepository = newPicturesRepository
        return newPicturesRepository
    }

    private fun createRemoteDataSource(): RemoteDataSourceImplementation =
        RemoteDataSourceImplementation(buildApiService())

    private fun buildNetworkStatusChecker(context: Context): NetworkStatusChecker =
        NetworkStatusChecker(context.getSystemService(ConnectivityManager::class.java))

}