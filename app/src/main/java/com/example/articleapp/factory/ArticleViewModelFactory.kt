package com.example.articleapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.articleapp.repository.ArticleRepository
import com.example.articleapp.viewModel.ArticleViewModel

class ArticleViewModelFactory(private val respository: ArticleRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleViewModel(respository) as T
    }

}