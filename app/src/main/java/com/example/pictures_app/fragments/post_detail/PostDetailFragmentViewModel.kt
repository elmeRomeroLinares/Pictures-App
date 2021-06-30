package com.example.pictures_app.fragments.post_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.PostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostDetailFragmentViewModel(postIdString: String?) : ViewModel() {

    private var postId: String? = postIdString
    private val repository = PicturesApplication.picturesRepository
    val post: MutableLiveData<PostModel> = MutableLiveData()

    init {
        getPost()
    }

    private fun getPost() {
        if (postId != null) {
            GlobalScope.launch(Dispatchers.IO) {
                post.postValue(
                    repository.getPostById((postId as String).toLong())
                )
            }

        } else {
            post.postValue(null)
        }
    }

    fun sharePostDetailByDynamicLink() {
    }
}