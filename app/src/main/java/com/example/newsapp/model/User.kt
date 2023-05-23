package com.example.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey (autoGenerate = true)
    var id : Int = 0,
    var firstName : String,
    var lastName : String,
    var userEmail : String,
    var userName : String,
    var gender : String
)
