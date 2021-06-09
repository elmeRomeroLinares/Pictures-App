package com.example.pictures_app.networking

import com.example.pictures_app.model.Failure
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.Result
import com.example.pictures_app.model.Success

const val BASE_URL = "https://jsonplaceholder.typicode.com"

class RemoteApi(private val apiService: RemoteApiService) {

    suspend fun remoteApiGetAlbum1Photos(): Result<List<PictureModel>> = try {
        val remoteData = apiService.getAlbum1Photos()
        Success(remoteData)
    } catch (error: Throwable) {
        Failure(error)
    }

}