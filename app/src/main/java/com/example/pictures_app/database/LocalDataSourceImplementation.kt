package com.example.pictures_app.database

import com.example.pictures_app.data.PicturesAlbumsPostsDataSource
import com.example.pictures_app.database.dao.AlbumsDao
import com.example.pictures_app.database.dao.PicturesDao
import com.example.pictures_app.database.dao.PostsDao
import com.example.pictures_app.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class LocalDataSourceImplementation internal constructor(
    private val picturesDao: PicturesDao,
    private val albumsDao: AlbumsDao,
    private val postsDao: PostsDao,
) : LocalDataSource {

    override suspend fun getAlbumPhotos(albumId: Long): Result<List<PictureModel>> = try {
        val localPhotosFromAlbumId = albumsDao.getPhotosByAlbum(albumId)?.pictures
        if (!localPhotosFromAlbumId.isNullOrEmpty()) {
            Success(localPhotosFromAlbumId)
        } else {
            Failure(Throwable())
        }
    } catch (error: Throwable) {
        Failure(error)
    }

    override suspend fun getAlbums(userId: Long): Result<List<AlbumPicturesModel>> = try {
        val localAlbums = albumsDao.getLocalAlbums()
        if (!localAlbums.isNullOrEmpty()) {
            Success(localAlbums)
        } else {
            Failure(Throwable())
        }
    } catch (error: Throwable) {
        Failure(error)
    }

    override suspend fun getPosts(userId: Long): Result<List<PostModel>> = try {
        val localPosts = postsDao.getLocalPosts()
        if (!localPosts.isNullOrEmpty()) {
            Success(localPosts)
        } else {
            Failure(Throwable())
        }
    } catch (error: Throwable) {
        Failure(error)
    }

    override suspend fun getPictureById(id: Long): Result<PictureModel> = try {
        val picture = picturesDao.getLocalPictureById(id)
        if (picture != null) {
            Success(picture)
        } else {
            Failure(Throwable())
        }
    } catch (error: Throwable) {
        Failure(error)
    }

    override suspend fun getPostById(id: Long): Result<PostModel> = try {
        val post = postsDao.getLocalPostById(id)
        if (post != null) {
            Success(post)
        } else {
            Failure(Throwable())
        }
    } catch (error: Throwable) {
        Failure(error)
    }

    override suspend fun addAlbumsToDataBase(albums: List<AlbumPicturesModel>) = try {
        albumsDao.addLocalAlbums(albums)
    } catch (error: Throwable) { }

    override suspend fun addPicturesToDataBase(pictures: List<PictureModel>) = try {
        picturesDao.addLocalPictures(pictures)
    } catch (error: Throwable) { }

    override suspend fun addPostsToDataBase(postsList: List<PostModel>) = try {
        postsDao.addLocalPosts(postsList)
    } catch (error: Throwable) { }

}