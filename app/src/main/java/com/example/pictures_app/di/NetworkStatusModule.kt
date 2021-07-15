package com.example.pictures_app.di

import com.example.pictures_app.networking.NetworkStatusChecker
import com.example.pictures_app.networking.NetworkStatusCheckerInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkStatusModule {

    @Binds
    abstract fun bindNetworkStatusChecker(
        implementation: NetworkStatusChecker
    ): NetworkStatusCheckerInterface
}