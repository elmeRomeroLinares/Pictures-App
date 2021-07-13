package com.example.pictures_app.data.source

import com.example.pictures_app.database.LocalDataSource
import com.example.pictures_app.model.*

class FakeLocalDataSource(
    private var picturesByAlbumList: LinkedHashMap<AlbumPicturesModel, List<PictureModel>>? = LinkedHashMap(),
    private var postsList: MutableList<PostModel>? = mutableListOf(),
    private var picturesList: MutableList<PictureModel>? = mutableListOf(),
    private var albumsList: MutableList<AlbumPicturesModel>? = mutableListOf()
) : LocalDataSource {

    override suspend fun getPictureById(id: Long): Result<PictureModel> {
        val picture = picturesList?.find { it.pictureId == id }
        return if (picture != null) {
            Success(picture)
        } else {
            Failure(Throwable())
        }
    }

    override suspend fun getPostById(id: Long): Result<PostModel> {
        val post = postsList?.find { it.postId == id }
        return if (post != null) {
            Success(post)
        } else {
            Failure(Throwable())
        }
    }

    override suspend fun addAlbumsToDataBase(albums: List<AlbumPicturesModel>) {
        for (album in albums)  {
            albumsList?.add(album)
        }
    }

    override suspend fun addPicturesToDataBase(pictures: List<PictureModel>) {
        for (picture in pictures) {
            picturesList?.add(picture)
        }
        albumsList?.let { safeAlbumsList ->
            val album = safeAlbumsList.find {it.albumId == pictures.first().pictureAlbumId}
            album?.let { albumKey ->
                picturesByAlbumList?.put(albumKey, pictures)
            }
        }
    }

    override suspend fun addPostsToDataBase(postsList: List<PostModel>) {
        for (post in postsList) {
            this.postsList?.add(post)
        }
    }

    override suspend fun getAlbumPhotos(albumId: Long): Result<List<PictureModel>> {
        val localPhotosFromAlbumId = picturesByAlbumList?.get(albumsList?.find {it.albumId == albumId})
        return if (!localPhotosFromAlbumId.isNullOrEmpty()) {
            Success(localPhotosFromAlbumId)
        } else {
            Failure(Throwable())
        }
    }

    override suspend fun getAlbums(userId: Long): Result<List<AlbumPicturesModel>> {
        val localAlbum = albumsList
        return if (!localAlbum.isNullOrEmpty()) {
            Success(localAlbum)
        } else {
            Failure(Throwable())
        }
    }

    override suspend fun getPosts(userId: Long): Result<List<PostModel>> {
        val localPosts = postsList
        return if (!localPosts.isNullOrEmpty()) {
            Success(localPosts)
        } else {
            Failure(Throwable())
        }
    }
}