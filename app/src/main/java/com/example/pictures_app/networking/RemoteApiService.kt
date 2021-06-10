package com.example.pictures_app.networking

import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteApiService {

    @GET("/albums/{albumId}/photos")
    suspend fun getAlbumPhotos(@Path("albumId") albumId: Long) : List<PictureModel>

    @GET("/users/{userId}/albums")
    suspend fun getUserAlbums(@Path("userId") userId: Long) : List<AlbumPicturesModel>
}