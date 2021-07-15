package com.example.pictures_app.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.data.PicturesAlbumsPostsDataSource
import com.example.pictures_app.database.LocalDataSource
import com.example.pictures_app.database.LocalDataSourceImplementation
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.model.Success
import com.example.pictures_app.networking.NetworkStatusChecker
import com.example.pictures_app.networking.NetworkStatusCheckerInterface
import com.example.pictures_app.networking.RemoteDataSourceImplementation
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_ID: Long = 1

@Singleton
class PicturesRepositoryImplementation @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: PicturesAlbumsPostsDataSource,
    private val networkStatusChecker: NetworkStatusCheckerInterface
) : PicturesRepository {

    override suspend fun getAllAlbums(): List<AlbumPicturesModel>? {
        return if (networkStatusChecker.hasInternetConnection()) {
            var result = remoteDataSource.getAlbums(USER_ID)
            if (result is Success) {
                onServerAlbumsListReceived(result.data)
            } else {
                result = localDataSource.getAlbums(USER_ID)
                if (result is Success) {
                    result.data
                } else {
                    null
                }
            }
        } else {
            val result = localDataSource.getAlbums(USER_ID)
            if (result is Success) {
                result.data
            } else {
                null
            }
        }
    }

    private suspend fun onServerAlbumsListReceived(
        albumsList: List<AlbumPicturesModel>
    ): List<AlbumPicturesModel> {
        val choppedAlbumsList = albumsList.dropLast(albumsList.size - 2)
        localDataSource.addAlbumsToDataBase(choppedAlbumsList)
        return choppedAlbumsList
    }

    override suspend fun getPicturesFromAlbumId(albumId: Long): List<PictureModel> {
        return if (networkStatusChecker.hasInternetConnection()) {
            var result = remoteDataSource.getAlbumPhotos(albumId = albumId)
            if (result is Success) {
                onServerPicturesListReceived(result.data)
            } else {
                result = localDataSource.getAlbumPhotos(albumId = albumId)
                if (result is Success) {
                    result.data
                } else {
                    emptyList()
                }
            }
        } else {
            val result = localDataSource.getAlbumPhotos(albumId = albumId)
            if (result is Success) {
                result.data
            } else {
                emptyList()
            }
        }
    }

    private suspend fun onServerPicturesListReceived(
        picturesList: List<PictureModel>
    ): List<PictureModel> {
        return if (picturesList.size > 25) {
            val choppedPicturesList = picturesList.dropLast(picturesList.size - 25)
            localDataSource.addPicturesToDataBase(choppedPicturesList)
            choppedPicturesList
        } else {
            localDataSource.addPicturesToDataBase(picturesList)
            picturesList
        }
    }

    override suspend fun getUserPosts(): List<PostModel>? {
        return if (networkStatusChecker.hasInternetConnection()) {
            var result = remoteDataSource.getPosts(USER_ID)
            if (result is Success) {
                onServerPostsListReceived(result.data)
            } else {
                result = localDataSource.getPosts(USER_ID)
                if (result is Success) {
                    result.data
                } else {
                    null
                }
            }
        } else {
            val result = remoteDataSource.getPosts(USER_ID)
            if (result is Success) {
                result.data
            } else {
                null
            }
        }
    }

    private suspend fun onServerPostsListReceived(postsList: List<PostModel>): List<PostModel> {
        localDataSource.addPostsToDataBase(postsList)
        return postsList
    }

    override suspend fun getPictureById(id: Long): PictureModel? {
        val picture = localDataSource.getPictureById(id)
        return if (picture is Success) {
            picture.data
        } else {
            null
        }
    }

    override suspend fun getPostById(id: Long): PostModel? {
        val post = localDataSource.getPostById(id)
        return if (post is Success) {
            post.data
        } else {
            null
        }
    }
}