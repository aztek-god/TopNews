package dv.serg.topnews.repository

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import dv.serg.topnews.current.SubSource
import io.reactivex.Flowable

@Dao
interface NewsResourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(subSource: List<SubSource>)

    @Query("SELECT * FROM news_resource")
    fun getAll(): Flowable<List<SubSource>>

    @Query("DELETE FROM news_resource")
    fun delete()
}