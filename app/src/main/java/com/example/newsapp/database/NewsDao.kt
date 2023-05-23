package com.example.newsapp.database

import androidx.room.*
import com.example.newsapp.model.News

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(news: News)

    @Query("SELECT * FROM news ORDER BY id DESC")
    suspend fun getDatabaseAllNews(): List<News>

    @Query("SELECT * FROM news WHERE content LIKE  '%' || :categoryType || '%'")
    fun getDatabaseCategoryNews(categoryType: String): List<News>

    @Query("SELECT * FROM news WHERE content LIKE  '%' || :searchQuery || '%'")
    fun searchNews(searchQuery: String): List<News>

    @Update
    suspend fun updateNews(news: News)

    @Delete
    suspend fun deleteNews(news: News)
}