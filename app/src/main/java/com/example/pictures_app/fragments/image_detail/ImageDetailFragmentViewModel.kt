package com.example.pictures_app.fragments.image_detail

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.repository.PicturesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageDetailFragmentViewModel @AssistedInject constructor(
    @Assisted private val pictureIdString: String?,
    private val repository: PicturesRepository
) : ViewModel() {

    var bitmap: Bitmap? = null
    val picture: MutableLiveData<PictureModel> = MutableLiveData()

    init {
        Log.d("Repository", repository.toString())
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

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(pictureIdString: String?): ImageDetailFragmentViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            pictureIdString: String?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>) =
                assistedFactory.create(pictureIdString) as T
        }
    }
}