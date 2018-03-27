package dv.serg.topnews.ui.livedata

import android.arch.lifecycle.LiveData
import dv.serg.topnews.dao.ArticleContract
import dv.serg.topnews.model.Article

class RecordLiveData(private val repo: ArticleContract.ArticleDao, private val type: Article.Type) : LiveData<Article>() {

    private var liveData: LiveData<List<Article>>? = null

    fun getData(): LiveData<List<Article>> {
        if (liveData == null) {
            liveData = repo.getAllByType(type)
        }

        return liveData as LiveData<List<Article>>
    }

}