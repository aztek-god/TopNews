package dv.serg.topnews.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerActivity
import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.model.SubSource
import dv.serg.topnews.ui.viewmodel.SubSourceViewModel
import io.reactivex.Flowable

@Module
class SubSourceModule {
    @Provides
    @PerActivity
    fun provideSubSourceVieModel(appDatabase: AppDatabase): SubSourceViewModel = SubSourceViewModel(
            object : SubSourceViewModel.Contract.Repository {
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
    )
}