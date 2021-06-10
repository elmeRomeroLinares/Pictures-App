package com.example.pictures_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "pictures_table")
class PictureModel(
    @field:Json(name = "albumId") val pictureAlbumId: Long?,
    @PrimaryKey
    @field:Json(name = "id") val pictureId: Long?,
    @field:Json(name = "title") val pictureTitle: String?,
    @field:Json(name = "url") val pictureUrl: String?,
    @field:Json(name = "thumbnailUrl") val pictureThumbnailUrl: String?)