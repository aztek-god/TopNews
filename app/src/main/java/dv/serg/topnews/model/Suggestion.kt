package dv.serg.topnews.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "suggestion")
data class Suggestion(@ColumnInfo(name = "suggestion_query") val suggestionQuery: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}