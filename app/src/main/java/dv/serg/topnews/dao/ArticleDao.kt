package dv.serg.topnews.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.Dao
import dv.serg.topnews.model.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article WHERE type = :type ORDER BY id DESC")
    fun getAllByType(type: Article.Type): LiveData<List<Article>>

    @Query("SELECT * FROM article")
    fun getAll(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)

    @Delete
    fun delete(article: Article)

    @Query("DELETE FROM article")
    fun deleteAll()

}