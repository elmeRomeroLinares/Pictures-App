package com.example.pictures_app.fragments.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.repository.PicturesRepository

@Suppress("UNCHECKED_CAST")
class AlbumsViewPagerFragmentViewModelFactory (
    private val repository: PicturesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (AlbumsViewPagerFragmentViewModel(repository) as T)

}