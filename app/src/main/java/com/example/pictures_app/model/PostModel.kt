package com.example.pictures_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "posts_table")
class PostModel (
    @field:Json(name = "userId") val postUserId: Long,
    @PrimaryKey
    @field:Json(name = "id") val postId: Long,
    @field:Json(name = "title") val postTitle: String,
    @field:Json(name = "body") val postBody: String)