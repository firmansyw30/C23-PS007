package com.capstone.trashtotreasure.model.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.capstone.trashtotreasure.BuildConfig
import com.capstone.trashtotreasure.model.data.local.entitiy.ArticleEntity
import com.capstone.trashtotreasure.model.data.local.room.NewsDao
import com.capstone.trashtotreasure.model.data.local.room.NewsDatabase
import com.capstone.trashtotreasure.model.data.Result
import com.capstone.trashtotreasure.model.data.remote.response.ArticleResponse
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiService
import com.capstone.trashtotreasure.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val newsDatabase: NewsDatabase,
    private val appExecutors: AppExecutors
) {

    private val result = MediatorLiveData<Result<List<ArticleEntity>>>()
    fun getArticle(): LiveData<Result<List<ArticleEntity>>> {
        result.value = Result.Loading
        val client = apiService.getArticle(BuildConfig.API_KEY)
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    val newsList = ArrayList<ArticleEntity>()
                    appExecutors.diskIO.execute {
                        articles?.forEach { article ->
                            val newss = newsDatabase.newsDao()
                            val isBookmarked = newss.isNewsBookmarked(article?.title.toString())
                            val news = ArticleEntity(
                                article?.title.toString(),
                                article?.publishedAt.toString(),
                                article?.urlToImage,
                                article?.url,
                                isBookmarked
                            )
                            newsList.add(news)
                        }
                        newsDao.deleteAll()
                        newsDao.insertNews(newsList)
                        result.postValue(Result.Success(newsList))
                    }
                }else{
                    result.postValue(Result.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = newsDao.getNews()
        result.addSource(localData) { newData: List<ArticleEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getBookmarkedNews(): LiveData<List<ArticleEntity>> {
        return newsDao.getBookmarkedNews()
    }

    fun setBookmarkedNews(news: ArticleEntity, bookmarkState: Boolean) {
        appExecutors.diskIO.execute {
            news.isBookmarked = bookmarkState
            newsDao.updateNews(news)
        }
    }

}