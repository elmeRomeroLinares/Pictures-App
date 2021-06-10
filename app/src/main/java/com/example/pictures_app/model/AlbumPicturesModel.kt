package com.example.pictures_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "albums_table")
class AlbumPicturesModel(
    @field:Json(name = "userId") val userId: Long?,
    @PrimaryKey
    @field:Json(name = "id") val albumId: Long?,
    @field:Json(name = "title") val albumTitle: String?
)