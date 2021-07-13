package com.example.pictures_app.data.source

import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel
import com.example.pictures_app.repository.PicturesRepository

class FakeTestRepository : PicturesRepository {
    override suspend fun getAllAlbums(): List<AlbumPicturesModel>? {
        TODO("Not yet implemented")
    }

    override suspend fun getPictureById(id: Long): PictureModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getPicturesFromAlbumId(albumId: Long): List<PictureModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserPosts(): List<PostModel>? {
        TODO("Not yet implemented")
    }

    override suspend fun getPostById(id: Long): PostModel? {
        TODO("Not yet implemented")
    }

}