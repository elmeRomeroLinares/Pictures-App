package com.example.pictures_app.fragments.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.repository.PicturesRepository

@Suppress("UNCHECKED_CAST")
class PostsListFragmentViewModelFactory(
    private val repository: PicturesRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (PostsListFragmentViewModel(repository) as T)
}