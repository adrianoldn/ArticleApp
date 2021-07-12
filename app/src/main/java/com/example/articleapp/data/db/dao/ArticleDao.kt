package com.example.articleapp.data.db.dao

import androidx.room.*
import com.example.articleapp.data.db.entity.ArticleEntity

@Dao
interface ArticleDao {

    @Query("Select * from Article")
    fun getArticles(): List<ArticleEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticle(articles: List<ArticleEntity>?)

    @Query("delete from Article")
    fun clear()
}