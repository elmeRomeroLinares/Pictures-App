package com.example.pictures_app.fragments.image_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.repository.PicturesRepository

@Suppress("UNCHECKED_CAST")
class ImageDetailFragmentViewModelFactory(
    private val pictureIdString: String?,
    private val repository: PicturesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (ImageDetailFragmentViewModel(pictureIdString, repository) as T)
}