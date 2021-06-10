package com.example.pictures_app.database.dao

import androidx.room.*
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PicturesByAlbum

@Dao
interface AlbumsDao {

    @Query("SELECT * FROM albums_table")
    suspend fun getLocalAlbums(): List<AlbumPicturesModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocalAlbums(albumsToAdd: List<AlbumPicturesModel>)

    @Transaction
    @Query("SELECT * FROM albums_table WHERE albumId = :id")
    suspend fun getPhotosByAlbum(id: Long): PicturesByAlbum
}