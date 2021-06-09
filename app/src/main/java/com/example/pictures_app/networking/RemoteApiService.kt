package com.example.pictures_app.networking

import com.example.pictures_app.model.PictureModel
import retrofit2.http.GET

interface RemoteApiService {

    @GET("/albums/1/photos")
    suspend fun getAlbum1Photos() : List<PictureModel>
}