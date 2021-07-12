package com.example.articleapp.repository

import com.example.articleapp.data.db.dao.ArticleDao
import com.example.articleapp.data.db.entity.ArticleEntity
import com.example.articleapp.model.ResponseArticle
import com.example.articleapp.network.AppRetrofit
import com.example.articleapp.util.UtilArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleRepository(private val dao: ArticleDao, private val api: AppRetrofit) {

    suspend fun getArticlesApi() {
        return withContext(Dispatchers.Default) {
            val articles = api.getArticleService().getArticles(UtilArticle.domain, UtilArticle.api_key)
            articles.enqueue(object : Callback<ResponseArticle> {
                override fun onFailure(call: Call<ResponseArticle>, t: Throwable) {
                    GlobalScope.launch {
                        clearArticles()
                        saveArticles(listOf())
                    }
                }

                override fun onResponse(
                    call: Call<ResponseArticle>,
                    response: Response<ResponseArticle>
                ) {
                   GlobalScope.launch {
                       clearArticles()
                       saveArticles(response.body()!!.articles)
                   }
                }

            })
        }
    }

    suspend fun fetchArticles(): List<ArticleEntity>? {
        return withContext(Dispatchers.Default) {
            getArticlesApi()
            dao.getArticles()
        }

    }

    suspend fun saveArticles (articles: List<ArticleEntity>){
        withContext(Dispatchers.IO){
            dao.saveArticle(articles)
        }
    }

     fun clearArticles (){
            dao.clear()
    }
}