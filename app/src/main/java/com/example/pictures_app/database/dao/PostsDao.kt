package com.example.pictures_app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pictures_app.model.PostModel

@Dao
interface PostsDao {

    @Query("SELECT * FROM posts_table")
    suspend fun getLocalPosts(): List<PostModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocalPosts(postsToAdd: List<PostModel>)

    @Query("SELECT * FROM posts_table WHERE postId = :id")
    suspend fun getLocalPostById(id: Long): PostModel
}