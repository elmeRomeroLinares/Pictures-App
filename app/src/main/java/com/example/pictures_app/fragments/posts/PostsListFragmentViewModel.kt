package com.example.pictures_app.fragments.posts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.repository.PicturesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsListFragmentViewModel @Inject constructor(
    private val repository: PicturesRepository
) : ViewModel() {

    val postsList: MutableLiveData<List<PostModel>> = MutableLiveData()

    init {
        Log.d("Repository", repository.toString())
        getPosts()
    }

    private fun getPosts() {
        GlobalScope.launch(Dispatchers.IO) {
            postsList.postValue(repository.getUserPosts())
        }
    }
}