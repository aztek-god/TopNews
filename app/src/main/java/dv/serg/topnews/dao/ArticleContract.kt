package dv.serg.topnews.dao

import android.arch.lifecycle.LiveData
import dv.serg.topnews.model.Article

interface ArticleContract {
    interface ArticleDao {
        fun getAll(): LiveData<List<Article>>
        fun getAllByType(type: Article.Type): LiveData<List<Article>>
        fun insert(article: Article)
        fun delete(article: Article)
        fun deleteAll()
    }
}