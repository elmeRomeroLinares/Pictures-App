package com.example.pictures_app.fragments.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.PostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostsListFragmentViewModel : ViewModel() {

    private val repository = PicturesApplication.picturesRepository
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