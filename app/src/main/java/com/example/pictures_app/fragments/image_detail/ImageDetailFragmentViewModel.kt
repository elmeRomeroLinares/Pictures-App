package com.example.pictures_app.fragments.image_detail

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.repository.PicturesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImageDetailFragmentViewModel(
    private val pictureIdString: String?,
    private val repository: PicturesRepository
) : ViewModel() {

    var bitmap: Bitmap? = null
    val picture: MutableLiveData<PictureModel> = MutableLiveData()

    init {
        getPicture()
    }

    override fun onCleared() {
        super.onCleared()
        bitmap = null
    }

    private fun getPicture() {
        if (pictureIdString != null) {
            GlobalScope.launch(Dispatchers.IO) {
                picture.postValue(
                    repository.getPictureById(pictureIdString.toLong())
                )
            }
        } else {
            picture.postValue(null)
        }
    }
}