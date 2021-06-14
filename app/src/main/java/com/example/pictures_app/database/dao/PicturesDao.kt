package com.example.pictures_app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pictures_app.model.PictureModel

@Dao
interface PicturesDao {

    @Query("SELECT * FROM pictures_table")
    suspend fun getLocalPictures(): List<PictureModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocalPictures(picturesToAdd: List<PictureModel>)

    @Query("SELECT * FROM pictures_table WHERE pictureId = :id")
    suspend fun getLocalPictureById(id: Long): PictureModel
}