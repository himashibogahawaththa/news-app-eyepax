package com.example.newsapp.database

import androidx.room.*
import com.example.newsapp.model.LatestNews

@Dao
interface LatestNewsDao {
    @Insert
    suspend fun addLatestNews(latestNews: LatestNews)

    @Query("SELECT * FROM LatestNews ORDER BY id DESC")
    suspend fun getLatestNews(): List<LatestNews>

    @Update
    suspend fun updateNews(latestNews: LatestNews)

    @Delete
    suspend fun deleteNews(latestNews: LatestNews)
}