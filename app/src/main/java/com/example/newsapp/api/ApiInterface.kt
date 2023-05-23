package com.example.newsapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET ("/v2/everything")
    suspend fun getAPINews(
        @Query("q")
        q : String,
    ): Response<ResponseList>

    @GET ("/v2/everything")
    suspend fun getAPILatestNews(
        @Query("q")
        q : String,
        @Query("sortBy")
        sortBy : String?
    ): Response<ResponseList>
}