package dv.serg.topnews.di.module

import android.arch.lifecycle.LiveData
import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerActivity
import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.dao.SuggestionDao
import dv.serg.topnews.model.Suggestion
import dv.serg.topnews.ui.viewmodel.SearchViewModel

@Module
@PerActivity
class SearchModule {
    @Provides
    @PerActivity
    fun provideSuggestionDao(appDatabase: AppDatabase): SearchViewModel.SearchDao {
        return object : SearchViewModel.SearchDao {
            private val room: SuggestionDao = appDatabase.suggestionDao()

            override fun getAllAsLiveData(): LiveData<List<Suggestion>> {
                return room.getAll()
            }

            override fun deleteAll() {
                room.deleteAll()
            }

            override fun insert(item: Suggestion) {
                room.insert(item)
            }
        }
    }

    @Provides
    @PerActivity
    fun provideViewModel(dao: SearchViewModel.SearchDao): SearchViewModel = SearchViewModel(dao)
}