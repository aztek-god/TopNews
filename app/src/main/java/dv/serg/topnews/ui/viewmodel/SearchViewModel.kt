package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.LiveData
import dv.serg.topnews.dao.Dao
import dv.serg.topnews.model.Suggestion
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val dao: SearchDao) {

    fun saveSuggestion(suggestion: Suggestion) {
        Completable.fromAction {
            dao.insert(suggestion)
        }.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).subscribe()
    }

    fun deleteAllSuggestions() {
        Completable.fromAction {
            dao.deleteAll()
        }.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe()
    }

    val suggestionData = dao.getAllAsLiveData()

    interface SearchDao : Dao<Suggestion> {
        fun getAllAsLiveData(): LiveData<List<Suggestion>>
        fun deleteAll()
    }
}