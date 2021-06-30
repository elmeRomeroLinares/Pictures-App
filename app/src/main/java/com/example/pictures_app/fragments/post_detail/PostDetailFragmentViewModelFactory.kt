package com.example.pictures_app.fragments.post_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class PostDetailFragmentViewModelFactory(
    private val postIdString: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostDetailFragmentViewModel::class.java)) {
            return PostDetailFragmentViewModel(postIdString) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}