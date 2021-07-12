package com.example.articleapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Article")
data class ArticleEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val content: String?,
    val publishedAt: String?
): Serializable{
}