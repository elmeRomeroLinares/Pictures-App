package com.example.pictures_app.repository

import androidx.lifecycle.MutableLiveData
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel

interface PicturesRepository {

    val picturesListLiveData: MutableLiveData<List<PictureModel>>?

    val albumsListLiveData: MutableLiveData<List<AlbumPicturesModel>>?

    val postsListLiveData: MutableLiveData<List<PostModel>>

    fun getAllAlbums()

    suspend fun getPictureById(id: Long): PictureModel

    fun getPicturesFromAlbumId(albumId: Long)

    fun getUserPosts()
}