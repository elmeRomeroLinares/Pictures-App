package com.example.pictures_app.database

import com.example.pictures_app.data.PicturesAlbumsPostsDataSource
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.model.Result

interface LocalDataSource : PicturesAlbumsPostsDataSource {
    suspend fun getPictureById(id: Long): Result<PictureModel>

    suspend fun getPostById(id: Long): Result<PostModel>

    suspend fun addAlbumsToDataBase(albums: List<AlbumPicturesModel>)

    suspend fun addPicturesToDataBase(pictures: List<PictureModel>)

    suspend fun addPostsToDataBase(postsList: List<PostModel>)
}