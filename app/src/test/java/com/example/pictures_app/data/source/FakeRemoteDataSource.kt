package com.example.pictures_app.data.source

import com.example.pictures_app.data.PicturesAlbumsPostsDataSource
import com.example.pictures_app.model.*

class FakeRemoteDataSource(
    private var picturesByAlbumList: LinkedHashMap<AlbumPicturesModel, List<PictureModel>>? = LinkedHashMap(),
    private var postsList: MutableList<PostModel>? = mutableListOf(),
    private var picturesList: MutableList<PictureModel>? = mutableListOf(),
    private var albumsList: MutableList<AlbumPicturesModel>? = mutableListOf()

) : PicturesAlbumsPostsDataSource {

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