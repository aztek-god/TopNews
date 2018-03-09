package dv.serg.topnews.app

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import dv.serg.topnews.current.SubSource
import dv.serg.topnews.repository.NewsResourceDao

@Database(entities = [SubSource::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsResourceDao(): NewsResourceDao
}