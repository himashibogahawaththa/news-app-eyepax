package com.example.newsapp.api

import com.example.newsapp.api.Articles
import com.google.gson.annotations.SerializedName

data class ResponseList(
    @SerializedName("articles")
    var articles: List<Articles>,
    @SerializedName ("totalResults")
    var totalResults: Int,
    @SerializedName ("status")
    var status: String
)
