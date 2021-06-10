package com.example.pictures_app.model

import androidx.room.Embedded
import androidx.room.Relation

class PicturesByAlbum(
    @Embedded
    val album: AlbumPicturesModel,
    @Relation(parentColumn = "albumId", entityColumn = "pictureAlbumId")
    val pictures: List<PictureModel>?
)