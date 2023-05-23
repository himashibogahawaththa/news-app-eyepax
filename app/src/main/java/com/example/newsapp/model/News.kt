package com.example.newsapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "news", indices = [Index(value = ["heading"], unique = true)])
data class News(
    var titleImage : String?,
    var heading : String,
    var date : String,
    var content : String,
    var publisher : String?,
    var description : String?,
    var contentUrl : String,
): Serializable{
    @PrimaryKey (autoGenerate = true) var id: Int = 0
}