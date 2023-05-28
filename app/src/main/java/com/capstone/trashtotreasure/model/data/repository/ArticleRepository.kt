package com.capstone.trashtotreasure.model.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.trashtotreasure.BuildConfig
import com.capstone.trashtotreasure.model.data.remote.response.ArticleResponse
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiService
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getArticle():LiveData<Result<ArticleResponse>> = liveData{
        try {
            val response = apiService.getArticle(BuildConfig.API_KEY)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }

    }

}