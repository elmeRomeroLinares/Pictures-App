package com.example.pictures_app.fragments.image_list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.fragments.image_detail.ImageDetailFragmentViewModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.repository.PicturesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImagesListFragmentViewModel @AssistedInject constructor(
    @Assisted albumIdLong: Long?,
    private val repository: PicturesRepository
) : ViewModel() {

    val albumId: MutableLiveData<Long> = MutableLiveData(albumIdLong)
    val picturesList: MutableLiveData<List<PictureModel>> = MutableLiveData()

    init {
        Log.d("Repository", repository.toString())
        getPicturesFromAlbumId()
    }

    private fun getPicturesFromAlbumId() {
        albumId.value?.let { nonNullAlbumId ->
            GlobalScope.launch(Dispatchers.IO) {
                picturesList.postValue(repository.getPicturesFromAlbumId(nonNullAlbumId))
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface ImagesListViewModelAssistedFactory {
        fun create(albumIdLong: Long?): ImagesListFragmentViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: ImagesListViewModelAssistedFactory,
            albumIdLong: Long?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) =
                assistedFactory.create(albumIdLong) as T
        }
    }


}