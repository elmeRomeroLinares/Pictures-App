package com.example.pictures_app.fragments.image_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageDetailFragmentViewModelFactory(
    private val pictureIdString: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageDetailFragmentViewModel::class.java)) {
            return ImageDetailFragmentViewModel(pictureIdString) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}