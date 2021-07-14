package com.example.pictures_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pictures_app.database.dao.AlbumsDao
import com.example.pictures_app.database.dao.PicturesDao
import com.example.pictures_app.database.dao.PostsDao
import com.example.pictures_app.model.AlbumPicturesModel
import com.example.pictures_app.model.PictureModel
import com.example.pictures_app.model.PostModel

const val DATABASE_VERSION = 1

@Database(
    entities = [PictureModel::class, AlbumPicturesModel::class, PostModel::class],
    version = DATABASE_VERSION
)
abstract class PicturesAppDatabase : RoomDatabase() {

    abstract fun picturesDao(): PicturesDao

    abstract fun albumsDao(): AlbumsDao

    abstract fun postsDao(): PostsDao
}