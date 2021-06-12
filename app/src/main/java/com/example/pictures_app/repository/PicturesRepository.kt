package com.example.pictures_app.repository

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel

interface PicturesRepository {

    val picturesListLiveData: MutableLiveData<List<PictureModel>>?

    val albumsListLiveData: MutableLiveData<List<AlbumPicturesModel>>?

    fun getAllAlbums()

    //one to many relation
    fun getPicturesFromAlbumId(albumId: Long)

    fun getSharePictureIntent(bitmap: Bitmap): Intent?
}