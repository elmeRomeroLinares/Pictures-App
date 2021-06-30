package com.example.pictures_app.fragments.image_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ImagesListFragmentViewModelFactory(
    private val albumIdLong: Long?
) :  ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImagesListFragmentViewModel::class.java)) {
            return ImagesListFragmentViewModel(albumIdLong) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}