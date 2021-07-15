package com.example.pictures_app.di

import com.example.pictures_app.data.PicturesAlbumsPostsDataSource
import com.example.pictures_app.database.LocalDataSource
import com.example.pictures_app.database.LocalDataSourceImplementation
import com.example.pictures_app.networking.RemoteDataSourceImplementation
import com.example.pictures_app.repository.PicturesRepository
import com.example.pictures_app.repository.PicturesRepositoryImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindPicturesRepository(
        implementation: PicturesRepositoryImplementation
    ): PicturesRepository

    @Binds
    abstract fun bindLocalDataSource(
        implementation: LocalDataSourceImplementation
    ): LocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(
        implementation: RemoteDataSourceImplementation
    ): PicturesAlbumsPostsDataSource
}