package com.example.pictures_app.fragments.post_detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.fragments.image_detail.ImageDetailFragmentViewModel
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.repository.PicturesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostDetailFragmentViewModel @AssistedInject constructor(
    @Assisted private val postIdString: String?,
    private val repository: PicturesRepository
) : ViewModel() {

    val post: MutableLiveData<PostModel> = MutableLiveData()

    init {
        Log.d("Repository", repository.toString())
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

    @dagger.assisted.AssistedFactory
    interface PostDetailViewModelAssistedFactory {
        fun create(postIdString: String?): PostDetailFragmentViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: PostDetailViewModelAssistedFactory,
            postIdString: String?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) =
                assistedFactory.create(postIdString) as T
        }
    }
}