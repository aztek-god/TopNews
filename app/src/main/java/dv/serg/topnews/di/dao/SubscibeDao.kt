package dv.serg.topnews.di.dao

import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.current.SubSource
import dv.serg.topnews.current.SubSourceViewModel
import io.reactivex.Flowable

class SubscibeDao(private val appDatabase: AppDatabase) : SubSourceViewModel.Contract.Repository {
    override fun getAll(): Flowable<List<SubSource>> {
        return appDatabase.newsResourceDao().getAll()
    }

    override fun deleteAll() {
        appDatabase.newsResourceDao().delete()
    }

    override fun insertAll(entities: List<SubSource>) {
        appDatabase.newsResourceDao().insertAll(entities)
    }
}