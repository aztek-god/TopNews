package dv.serg.topnews.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerFragment
import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.current.SubSource
import dv.serg.topnews.current.SubSourceViewModel
import dv.serg.topnews.ui.viewmodel.HotNewsViewModel
import dv.serg.topnews.ui.viewmodel.NewsViewModel
import io.reactivex.Flowable
import retrofit2.Retrofit

@Suppress("UNCHECKED_CAST")
@Module
@PerFragment
class FragmentModule {
    @PerFragment
    @Provides
    fun provideViewModelFactory(retrofit: Retrofit, database: AppDatabase): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    with(modelClass) {
                        when {
                            isAssignableFrom(HotNewsViewModel::class.java) -> HotNewsViewModel(retrofit)
                            isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(retrofit)
                            isAssignableFrom(SubSourceViewModel::class.java) -> SubSourceViewModel(object : SubSourceViewModel.Contract.Repository {
                                override fun getAll(): Flowable<List<SubSource>> {
                                    return database.newsResourceDao().getAll()
                                }

                                override fun deleteAll() {
                                    database.newsResourceDao().delete()
                                }

                                override fun insertAll(entities: List<SubSource>) {
                                    database.newsResourceDao().insertAll(entities)
                                }
                            })
                            else
                            -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                        }
                    } as T
        }
    }


}