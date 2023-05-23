package com.example.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class LatestNews(
    var titleImage : String?,
    var heading : String?,
    var content : String,
    var date : String,
    var publisher : String?,
    var description : String?,
    var contentUrl : String,
    var category : String?,
): Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
