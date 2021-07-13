package com.example.pictures_app.data

import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.model.Result

interface PicturesAlbumsPostsDataSource {

    suspend fun getAlbumPhotos(albumId: Long): Result<List<PictureModel>>

    suspend fun getAlbums(userId: Long): Result<List<AlbumPicturesModel>>

    suspend fun getPosts(userId: Long): Result<List<PostModel>>
}