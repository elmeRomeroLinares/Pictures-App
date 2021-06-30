package com.example.pictures_app.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import com.example.pictures_app.PicturesApplication
import com.example.pictures_app.database.dao.AlbumsDao
import com.example.pictures_app.database.dao.PicturesDao
import com.example.pictures_app.database.dao.PostsDao
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.model.Success
import com.example.pictures_app.networking.NetworkStatusChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val USER_ID: Long = 1

class PicturesRepositoryImplementation(
    private val picturesDao: PicturesDao,
    private val albumsDao: AlbumsDao,
    private val postsDao: PostsDao,
    private val context: Context
) : PicturesRepository {

    private val remoteApi = PicturesApplication.remoteApi
    private val networkStatusChecker by lazy {
        NetworkStatusChecker(context.getSystemService(ConnectivityManager::class.java))
    }
    override val picturesListLiveData: MutableLiveData<List<PictureModel>> = MutableLiveData()
    override val postsListLiveData: MutableLiveData<List<PostModel>> = MutableLiveData()

    override suspend fun getAllAlbums(): List<AlbumPicturesModel>? {
        if (networkStatusChecker.hasInternetConnection()) {
            val result = remoteApi.remoteApiGetAlbums(USER_ID)
            return if (result is Success) {
                onServerAlbumsListReceived(result.data)
            } else {
                null
            }
        } else if (getAllLocalAlbums().isEmpty()) {
            return null
        } else {
            return getAllLocalAlbums()
        }
    }

    override suspend fun getPictureById(id: Long): PictureModel =
        picturesDao.getLocalPictureById(id)

    override suspend fun getPicturesFromAlbumId(albumId: Long): List<PictureModel> {
        if (networkStatusChecker.hasInternetConnection()) {
            val result = remoteApi.remoteApiGetAlbumPhotos(albumId = albumId)
            return if (result is Success) {
                onServerPicturesListReceived(result.data)
            } else {
                emptyList()
            }
        } else if (getAllLocalPicturesByAlbumId(albumId).isEmpty()) {
            return emptyList()
        } else {
            return getAllLocalPicturesByAlbumId(albumId)
        }
    }

    override suspend fun getUserPosts(): List<PostModel>? {
        if (networkStatusChecker.hasInternetConnection()) {
            val result = remoteApi.remoteApiGetPosts(USER_ID)
            if (result is Success) {
                return onServerPostsListReceived(result.data)
            } else {
                return null
            }
        } else if (getAllLocalPosts().isEmpty()) {
            return null
        } else {
            return getAllLocalPosts()
        }
    }

    override suspend fun getPostById(id: Long): PostModel =
        postsDao.getLocalPostById(id)

    private suspend fun getAllLocalAlbums(): List<AlbumPicturesModel> = albumsDao.getLocalAlbums()

    private suspend fun addAlbumsToDataBase(albums: List<AlbumPicturesModel>) =
        albumsDao.addLocalAlbums(albums)

    private suspend fun onServerAlbumsListReceived(
        albumsList: List<AlbumPicturesModel>
    ): List<AlbumPicturesModel> {
        val choppedAlbumsList = albumsList.dropLast(albumsList.size - 2)
        addAlbumsToDataBase(choppedAlbumsList)
        return choppedAlbumsList
    }

    private suspend fun getAllLocalPicturesByAlbumId(albumId: Long): List<PictureModel> {
        val picturesFromAlbum = albumsDao.getPhotosByAlbum(albumId).pictures
        return picturesFromAlbum?: emptyList()
    }

    private suspend fun addPicturesToDataBase(pictures: List<PictureModel>) =
        picturesDao.addLocalPictures(pictures)

    private suspend fun onServerPicturesListReceived(
        picturesList: List<PictureModel>
    ): List<PictureModel> {
        val choppedPicturesList = picturesList.dropLast(picturesList.size - 25)
        addPicturesToDataBase(choppedPicturesList)
        return choppedPicturesList
    }

    private suspend fun getAllLocalPosts(): List<PostModel> = postsDao.getLocalPosts()

    private suspend fun addPostsToDataBase(postsList: List<PostModel>) =
        postsDao.addLocalPosts(postsList)

    private suspend fun onServerPostsListReceived(postsList: List<PostModel>): List<PostModel> {
        addPostsToDataBase(postsList)
        return postsList
    }
}