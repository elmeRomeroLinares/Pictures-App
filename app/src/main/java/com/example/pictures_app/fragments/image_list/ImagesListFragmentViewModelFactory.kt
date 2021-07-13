package com.example.pictures_app.fragments.image_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.repository.PicturesRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ImagesListFragmentViewModelFactory(
    private val albumIdLong: Long?,
    private val repository: PicturesRepository
) :  ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (ImagesListFragmentViewModel(albumIdLong, repository) as T)
}