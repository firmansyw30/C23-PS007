package com.capstone.trashtotreasure.model.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.capstone.trashtotreasure.model.data.local.entitiy.ArticleEntity
import com.capstone.trashtotreasure.model.data.repository.ArticleRepository

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER BY publishedAt DESC")
    fun getNews(): LiveData<List<ArticleEntity>>

    @Query("SELECT * FROM news where bookmarked = 1")
    fun getBookmarkedNews(): LiveData<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNews(news: List<ArticleEntity>)

    @Update
    fun updateNews(news: ArticleEntity)

    @Query("DELETE FROM news WHERE bookmarked = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM news WHERE title = :title AND bookmarked = 1)")
    fun isNewsBookmarked(title: String): Boolean
}