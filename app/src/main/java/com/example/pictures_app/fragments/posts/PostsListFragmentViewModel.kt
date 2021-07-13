package com.example.pictures_app.fragments.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.repository.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostsListFragmentViewModel(
    private val repository: PicturesRepository
) : ViewModel() {

    val postsList: MutableLiveData<List<PostModel>> = MutableLiveData()

    init {
        getPosts()
    }

    private fun getPosts() {
        GlobalScope.launch(Dispatchers.IO) {
            postsList.postValue(repository.getUserPosts())
        }
    }
}