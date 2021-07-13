package com.example.pictures_app.networking

import com.example.pictures_app.data.PicturesAlbumsPostsDataSource
import com.example.pictures_app.model.*

const val BASE_URL = "https://jsonplaceholder.typicode.com"

class RemoteDataSourceImplementation (
    private val remoteApiService: RemoteApiService
) : PicturesAlbumsPostsDataSource {

    override suspend fun getAlbumPhotos(albumId: Long): Result<List<PictureModel>> = try {
        val remoteData = remoteApiService.getAlbumPhotos(albumId = albumId)
        if (!remoteData.isNullOrEmpty()) {
            Success(remoteData)
        } else {
            Failure(Throwable())
        }
    } catch (error: Throwable) {
        Failure(error)
    }

    override suspend fun getAlbums(userId: Long): Result<List<AlbumPicturesModel>> = try {
        val remoteAlbums = remoteApiService.getUserAlbums(userId = userId)
        if (!remoteAlbums.isNullOrEmpty()) {
            Success(remoteAlbums)
        } else {
            Failure(Throwable())
        }
    } catch (error: Throwable) {
        Failure(error)
    }

    override suspend fun getPosts(userId: Long): Result<List<PostModel>> = try {
        val remotePosts = remoteApiService.getUserPosts(userId = userId)
        if (!remotePosts.isNullOrEmpty()) {
            Success(remotePosts)
        } else {
            Failure(Throwable())
        }
    } catch (error: Throwable) {
        Failure(error)
    }
}