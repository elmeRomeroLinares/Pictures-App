package com.example.pictures_app.fragments.image_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.repository.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImagesListFragmentViewModel(
    albumIdLong: Long?,
    private val repository: PicturesRepository
) : ViewModel() {

    val albumId: MutableLiveData<Long> = MutableLiveData(albumIdLong)
    val picturesList: MutableLiveData<List<PictureModel>> = MutableLiveData()

    init {
        getPicturesFromAlbumId()
    }

    private fun getPicturesFromAlbumId() {
        albumId.value?.let { nonNullAlbumId ->
            GlobalScope.launch(Dispatchers.IO) {
                picturesList.postValue(repository.getPicturesFromAlbumId(nonNullAlbumId))
            }
        }
    }

}