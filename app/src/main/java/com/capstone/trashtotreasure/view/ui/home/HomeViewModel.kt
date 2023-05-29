package com.capstone.trashtotreasure.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.trashtotreasure.model.data.local.entitiy.ArticleEntity
import com.capstone.trashtotreasure.model.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
): ViewModel() {
    fun getAllArticle()= articleRepository.getArticle()

    fun getBookmarkedNews() = articleRepository.getBookmarkedNews()

    fun saveNews(news: ArticleEntity) {
        articleRepository.setBookmarkedNews(news, true)
    }

    fun deleteNews(news: ArticleEntity) {
        articleRepository.setBookmarkedNews(news, false)
    }
}