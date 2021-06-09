package com.example.pictures_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "pictures_table")
class PictureModel(
    @field:Json(name = "albumId") val albumId: Long?,
    @PrimaryKey
    @field:Json(name = "id") val id: Long?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "thumbnailUrl") val thumbnailUrl: String?)