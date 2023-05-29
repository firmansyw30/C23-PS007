package com.capstone.trashtotreasure.model.data.remote.retrofit

import com.capstone.trashtotreasure.model.data.remote.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything?q=plastic")
     fun getArticle(@Query("apiKey") apiKey: String): Call<ArticleResponse>
}