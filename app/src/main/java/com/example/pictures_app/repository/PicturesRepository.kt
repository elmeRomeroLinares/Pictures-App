package com.example.pictures_app.repository

import androidx.lifecycle.MutableLiveData
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel

interface PicturesRepository {

    suspend fun getAllAlbums(): List<AlbumPicturesModel>?

    suspend fun getPictureById(id: Long): PictureModel?

    suspend fun getPicturesFromAlbumId(albumId: Long): List<PictureModel>

    suspend fun getUserPosts(): List<PostModel>?

    suspend fun getPostById(id: Long): PostModel?
}