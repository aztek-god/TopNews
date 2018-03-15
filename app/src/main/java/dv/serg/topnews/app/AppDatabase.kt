package dv.serg.topnews.app

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import dv.serg.topnews.dao.ArticleDao
import dv.serg.topnews.dao.NewsResourceDao
import dv.serg.topnews.model.Article
import dv.serg.topnews.model.SubSource

@Database(entities = [SubSource::class, Article::class], version = 1, exportSchema = false)
@TypeConverters(Article.TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsResourceDao(): NewsResourceDao
    abstract fun newsArticleDao(): ArticleDao
}