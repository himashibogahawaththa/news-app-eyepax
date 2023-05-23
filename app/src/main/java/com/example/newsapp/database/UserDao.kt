package com.example.newsapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.newsapp.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user ORDER BY id DESC")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM user WHERE userName = :userName")
    suspend fun getUser(userName: String): List<User>

    @Update
    suspend fun editUser(user: User)
}