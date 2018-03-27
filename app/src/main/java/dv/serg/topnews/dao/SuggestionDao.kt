package dv.serg.topnews.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import dv.serg.topnews.model.Suggestion

@Dao
interface SuggestionDao {

    @Query("SELECT * FROM suggestion ORDER BY id DESC")
    fun getAll(): LiveData<List<Suggestion>>

    @Query("DELETE FROM suggestion")
    fun deleteAll()

    @Insert
    fun insert(suggestion: Suggestion)
}