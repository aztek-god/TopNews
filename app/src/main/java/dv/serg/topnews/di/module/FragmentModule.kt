package dv.serg.topnews.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerFragment
import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.current.SubSourceViewModel
import dv.serg.topnews.dao.ArticleRoomService
import dv.serg.topnews.di.dao.SubscibeDao
import dv.serg.topnews.ui.viewmodel.HotNewsViewModel
import dv.serg.topnews.ui.viewmodel.NewsViewModel
import dv.serg.topnews.ui.viewmodel.RecordViewModel
import retrofit2.Retrofit

@Suppress("UNCHECKED_CAST")
@Module
@PerFragment
class FragmentModule {
    @PerFragment
    @Provides
    fun provideViewModelFactory(retrofit: Retrofit, database: AppDatabase): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            private val subscribeDatabase = SubscibeDao(database)

            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    with(modelClass) {
                        when {
                            isAssignableFrom(HotNewsViewModel::class.java) -> HotNewsViewModel(retrofit, ArticleRoomService(database))
                            isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(retrofit, subscribeDatabase)
                            isAssignableFrom(SubSourceViewModel::class.java) -> SubSourceViewModel(subscribeDatabase)
                            isAssignableFrom(RecordViewModel::class.java) -> RecordViewModel(ArticleRoomService(database))
                            else ->
                                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                        }
                    } as T
        }
    }


}