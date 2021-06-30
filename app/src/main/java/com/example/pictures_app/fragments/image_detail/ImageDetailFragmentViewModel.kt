package com.example.pictures_app.fragments.image_detail

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.model.PictureModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImageDetailFragmentViewModel(pictureIdString: String?) : ViewModel() {

    private var pictureId: String? = pictureIdString
    private val repository = PicturesApplication.picturesRepository
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
        if (pictureId != null) {
            GlobalScope.launch(Dispatchers.IO) {
                picture.postValue(
                    repository.getPictureById((pictureId as String).toLong())
                )
            }
        } else {
            picture.postValue(null)
        }
    }
}