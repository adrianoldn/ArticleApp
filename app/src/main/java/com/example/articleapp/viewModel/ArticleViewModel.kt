package com.example.articleapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.articleapp.data.db.entity.ArticleEntity
import com.example.articleapp.model.ResponseArticle
import com.example.articleapp.repository.ArticleRepository
import kotlinx.coroutines.*

class ArticleViewModel (private val respository: ArticleRepository): ViewModel(){

    val articlesLiveData = MutableLiveData<List<ArticleEntity>>()

    suspend fun fetchArticles(){

            var articles = withContext(Dispatchers.Default) {
                respository.fetchArticles()
            }
            articlesLiveData.postValue(articles)
        }
    }
