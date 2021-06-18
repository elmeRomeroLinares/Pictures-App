package com.example.pictures_app.networking

import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteApiService {

    @GET("/albums/{albumId}/photos")
    suspend fun getAlbumPhotos(@Path("albumId") albumId: Long): List<PictureModel>

    @GET("/users/{userId}/albums")
    suspend fun getUserAlbums(@Path("userId") userId: Long): List<AlbumPicturesModel>

    @GET("/posts")
    suspend fun getUserPosts(@Query("userId") userId: Long): List<PostModel>
}