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

    @Volatile
    var picturesRepository: PicturesRepository? = null @VisibleForTesting set

    fun providePicturesRepository(
        localDataSourceImplementation: LocalDataSourceImplementation,
        remoteDataSourceImplementation: RemoteDataSourceImplementation,
        networkStatusChecker: NetworkStatusChecker
    ): PicturesRepository {
        synchronized(this) {
            return picturesRepository ?: createPicturesRepository(localDataSourceImplementation, remoteDataSourceImplementation, networkStatusChecker)
        }
    }

    private fun createPicturesRepository(
        localDataSourceImplementation: LocalDataSourceImplementation,
        remoteDataSourceImplementation: RemoteDataSourceImplementation,
        networkStatusChecker: NetworkStatusChecker
    ): PicturesRepository {
        val newPicturesRepository = PicturesRepositoryImplementation(
            localDataSourceImplementation,
            remoteDataSourceImplementation,
            networkStatusChecker
        )
        picturesRepository = newPicturesRepository
        return newPicturesRepository
    }
}