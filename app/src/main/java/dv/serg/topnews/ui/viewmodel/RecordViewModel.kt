package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.RecyclerView
import android.view.View
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.dao.ArticleContract
import dv.serg.topnews.model.Article

class RecordViewModel(private val repo: ArticleContract.ArticleDao) : ViewModel() {

    var type: Article.Type? = null

    var adapter: StandardAdapter<Article, RecordViewHolder>? = null

    val articles: LiveData<List<Article>> by lazy {
        if (type == null) {
            repo.getAll()
        } else {
            repo.getAllByType(type as Article.Type)
        }
    }

    abstract class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Article, RecordViewHolder>
}