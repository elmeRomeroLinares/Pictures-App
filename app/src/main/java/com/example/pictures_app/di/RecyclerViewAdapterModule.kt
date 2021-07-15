package com.example.pictures_app.di

import com.example.pictures_app.adapters.PostsRecyclerViewAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
class RecyclerViewAdapterModule {

    @Provides
    @Singleton
    fun providesPostsRecyclerViewAdapter(): PostsRecyclerViewAdapter = PostsRecyclerViewAdapter()
}