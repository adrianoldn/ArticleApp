package com.example.articleapp.network

import com.example.articleapp.model.ResponseArticle
import retrofit2.Call
import retrofit2.http.GET

interface ArticleService {
    @GET("everything")
    fun getArticles(
      @retrofit2.http.Query("domains") domain: String,
      @retrofit2.http.Query("apiKey") apiKey: String
    ): Call<ResponseArticle>
}