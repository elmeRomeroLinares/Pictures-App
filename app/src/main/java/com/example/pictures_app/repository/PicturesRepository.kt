package com.example.pictures_app.repository

import androidx.lifecycle.MutableLiveData
import com.example.pictures_app.model.PictureModel

interface PicturesRepository {

    val picturesListLiveData: MutableLiveData<List<PictureModel>>?

    fun getPictures()
}