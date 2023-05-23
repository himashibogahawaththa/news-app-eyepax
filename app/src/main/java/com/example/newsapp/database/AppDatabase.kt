package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.model.News
import com.example.newsapp.model.LatestNews
import com.example.newsapp.model.User

@Database(entities = [News::class, LatestNews::class, User::class], version = 2)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getNewsDao(): NewsDao
    abstract fun getLatestNewsDao(): LatestNewsDao
    abstract fun getUserDao(): UserDao

    companion object{

        @Volatile
        private var instance: AppDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){ //synchronized - line by line like interpreter
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).allowMainThreadQueries().build()
    }
}