package com.example.articleapp.model

import com.example.articleapp.data.db.entity.ArticleEntity

class ResponseArticle(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleEntity>
) {

}
