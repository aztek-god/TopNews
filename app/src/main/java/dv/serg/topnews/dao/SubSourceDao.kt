package dv.serg.topnews.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import dv.serg.topnews.model.SubSource
import io.reactivex.Flowable

@Dao
interface SubSourceDao {

    @Query(value = "SELECT * FROM news_resource")
    fun getAll(): Flowable<List<SubSource>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entities: List<SubSource>)

    @Query("DELETE FROM news_resource")
    fun deleteAll()

}