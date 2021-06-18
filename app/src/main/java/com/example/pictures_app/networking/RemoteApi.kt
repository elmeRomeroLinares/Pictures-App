package com.example.pictures_app.networking

import com.example.pictures_app.model.*

const val BASE_URL = "https://jsonplaceholder.typicode.com"

class RemoteApi(private val apiService: RemoteApiService) {

    suspend fun remoteApiGetAlbumPhotos(albumId: Long): Result<List<PictureModel>> = try {
        val remoteData = apiService.getAlbumPhotos(albumId = albumId)
        Success(remoteData)
    } catch (error: Throwable) {
        Failure(error)
    }

    suspend fun remoteApiGetAlbums(userId: Long): Result<List<AlbumPicturesModel>> = try {
        val remoteAlbums = apiService.getUserAlbums(userId = userId)
        Success(remoteAlbums)
    } catch (error: Throwable) {
        Failure(error)
    }

    suspend fun remoteApiGetPosts(userId: Long): Result<List<PostModel>> = try {
        val remotePosts = apiService.getUserPosts(userId = userId)
        Success(remotePosts)
    } catch (error: Throwable) {
        Failure(error)
    }
}