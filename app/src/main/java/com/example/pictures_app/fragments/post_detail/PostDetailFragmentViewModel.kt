package com.example.pictures_app.fragments.post_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.repository.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostDetailFragmentViewModel(
    private val postIdString: String?,
    private val repository: PicturesRepository
) : ViewModel() {

    val post: MutableLiveData<PostModel> = MutableLiveData()

    init {
        getPost()
    }

    private fun getPost() {
        if (postIdString != null) {
            GlobalScope.launch(Dispatchers.IO) {
                post.postValue(
                    repository.getPostById((postIdString as String).toLong())
                )
            }
        } else {
            post.postValue(null)
        }
    }
}