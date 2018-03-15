package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.ViewModel
import android.support.v7.view.ActionMode
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.logd
import dv.serg.topnews.model.SubSource
import dv.serg.topnews.model.getResources
import dv.serg.topnews.ui.holder.SubSourceViewHolder
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


// todo repo for saving user selected resources
class SubSourceViewModel(private val repo: Contract.Repository) : ViewModel() {

    var subSourceAdapter: StandardAdapter<SubSource, SubSourceViewHolder>? = null

    val isActionMode: Boolean get() = availableSources.any { it.isSelected }

    var actionMode: ActionMode? = null

    val selectedCount: Int get() = subSourceAdapter?.count { it.isSelected } ?: 0

    // todo fix encapsulation
    val availableSources: List<SubSource> = getResources()

    fun save(entities: List<SubSource>, action: () -> Unit) {
        logd("SubSourceViewModel:save{entities = $entities}")
        Flowable.fromCallable {
            repo.deleteAll()
        }
                .flatMap {
                    Flowable.fromCallable {
                        repo.insertAll(entities)
                    }
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {
                }, {
                    action.invoke()
                })
    }

    interface Contract {
        interface Repository {
            fun getAll(): Flowable<List<SubSource>>
            fun deleteAll()
            fun insertAll(entities: List<SubSource>)
        }
    }

}