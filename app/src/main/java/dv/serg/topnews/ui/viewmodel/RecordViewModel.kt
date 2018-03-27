package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.RecyclerView
import android.view.View
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.dao.ArticleContract
import dv.serg.topnews.exts.performOnIoThread
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.livedata.RecordLiveData
import io.reactivex.Completable

class RecordViewModel(private val repo: ArticleContract.ArticleDao) : ViewModel() {

    var type: Article.Type? = null

    val mRecordLiveData: RecordLiveData by lazy { RecordLiveData(repo, type!!) }

    val articles: LiveData<List<Article>> by lazy {
        if (type == null) {
            repo.getAll()
        } else {
            repo.getAllByType(type as Article.Type)
        }
    }

    fun delete(article: Article, action: (Article) -> Unit = {}) {
        Completable.fromAction {
            repo.delete(article)
        }.performOnIoThread<Article>().doOnComplete {
            action.invoke(article)
        }.subscribe()
    }

    fun insert(article: Article, action: (Article) -> Unit = {}) {
        Completable.fromAction {
            repo.insert(article)
        }.performOnIoThread<Article>().doOnComplete { action.invoke(article) }.subscribe()
    }

    abstract class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Article, RecordViewHolder>
}