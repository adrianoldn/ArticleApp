package com.example.articleapp.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class AppRetrofit () {

    val url = "https://newsapi.org/v2/"

    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val httpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getArticleService(): ArticleService{
        return retrofit.create(ArticleService::class.java)
    }
}