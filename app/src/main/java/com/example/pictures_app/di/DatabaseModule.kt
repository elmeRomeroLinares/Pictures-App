package com.example.pictures_app.di

import android.content.Context
import androidx.room.Room
import com.example.pictures_app.database.PicturesAppDatabase
import com.example.pictures_app.database.dao.AlbumsDao
import com.example.pictures_app.database.dao.PicturesDao
import com.example.pictures_app.database.dao.PostsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "PicturesDB"
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun providePicturesDatabase(@ApplicationContext appContext: Context): PicturesAppDatabase {
        return Room.databaseBuilder(
            appContext,
            PicturesAppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun providePicturesDao(database: PicturesAppDatabase): PicturesDao {
        return database.picturesDao()
    }

    @Provides
    fun provideAlbumsDao(database: PicturesAppDatabase): AlbumsDao {
        return database.albumsDao()
    }

    @Provides
    fun providePostsDao(database: PicturesAppDatabase): PostsDao {
        return database.postsDao()
    }


}