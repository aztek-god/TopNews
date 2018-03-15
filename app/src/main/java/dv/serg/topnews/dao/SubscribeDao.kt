package dv.serg.topnews.dao

import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.model.SubSource
import dv.serg.topnews.ui.viewmodel.SubSourceViewModel
import io.reactivex.Flowable

class SubscribeDao(private val appDatabase: AppDatabase) : SubSourceViewModel.Contract.Repository {
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