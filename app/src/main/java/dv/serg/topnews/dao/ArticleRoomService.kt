package dv.serg.topnews.dao

import android.arch.lifecycle.LiveData
import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.model.Article

class ArticleRoomService(private val appDatabase: AppDatabase) : ArticleContract.ArticleDao {

    private val roomDao: ArticleDao by lazy {
        appDatabase.newsArticleDao()
    }

    override fun getAllByType(type: Article.Type): LiveData<List<Article>> {
        return roomDao.getAllByType(type)
    }

    override fun getAll(): LiveData<List<Article>> {
        return roomDao.getAll()
    }

    override fun insert(article: Article) {
        roomDao.insert(article)
    }

    override fun delete(article: Article) {
        roomDao.delete(article)
    }

    override fun deleteAll() {
        roomDao.deleteAll()
    }
}