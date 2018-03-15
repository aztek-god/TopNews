package dv.serg.topnews.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerFragment
import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.dao.ArticleRoomService
import dv.serg.topnews.dao.SubscribeDao
import dv.serg.topnews.ui.viewmodel.HotNewsViewModel
import dv.serg.topnews.ui.viewmodel.NewsViewModel
import dv.serg.topnews.ui.viewmodel.RecordViewModel
import dv.serg.topnews.ui.viewmodel.SubSourceViewModel
import retrofit2.Retrofit

@Suppress("UNCHECKED_CAST")
@Module
@PerFragment
class FragmentModule {
    @PerFragment
    @Provides
    fun provideViewModelFactory(retrofit: Retrofit, database: AppDatabase): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            private val subscribeDatabase: SubscribeDao = SubscribeDao(database)
            private val articleRepo = ArticleRoomService(database)

            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    with(modelClass) {
                        when {
                            isAssignableFrom(HotNewsViewModel::class.java) -> HotNewsViewModel(retrofit, articleRepo)
                            isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(retrofit, subscribeDatabase, articleRepo)
                            isAssignableFrom(SubSourceViewModel::class.java) -> SubSourceViewModel(subscribeDatabase)
                            isAssignableFrom(RecordViewModel::class.java) -> RecordViewModel(articleRepo)
                            else ->
                                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                        }
                    } as T
        }
    }


}