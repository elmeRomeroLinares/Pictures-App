package com.example.pictures_app.fragments.post_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.repository.PicturesRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class PostDetailFragmentViewModelFactory(
    private val postIdString: String?,
    private val repository: PicturesRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        (PostDetailFragmentViewModel(postIdString, repository) as T)
}