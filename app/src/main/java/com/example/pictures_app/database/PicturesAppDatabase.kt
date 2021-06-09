package com.example.pictures_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pictures_app.database.dao.PicturesDao
import com.example.pictures_app.model.PictureModel

const val DATABASE_VERSION = 1

@Database(
    entities = [PictureModel::class],
    version = DATABASE_VERSION
)

abstract class PicturesAppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "PicturesDB"

        fun buildDatabase(context: Context): PicturesAppDatabase {
            return Room.databaseBuilder(
                context,
                PicturesAppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

    abstract fun picturesDao(): PicturesDao
}