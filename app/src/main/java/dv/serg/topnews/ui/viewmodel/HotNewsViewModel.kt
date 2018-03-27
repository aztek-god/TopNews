package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import dv.serg.topnews.dao.ArticleContract
import dv.serg.topnews.data.Outcome
import dv.serg.topnews.model.Article
import dv.serg.topnews.model.Response
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class HotNewsViewModel(private val retrofit: Retrofit, private val repo: ArticleContract.ArticleDao) : ViewModel() {

    private interface NewsService {
        @GET("top-headlines")
        fun request(@Query("category") category: String, @Query("country") country: String): Flowable<Response>
    }

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val liveNewsResult: MutableLiveData<Outcome<List<Article>>> = MutableLiveData()
    val output: LiveData<Outcome<List<Article>>> get() = liveNewsResult

    var isFirstLaunched = true

    private var mSavedDataPerRotation: MutableList<Article> = mutableListOf()

    val restoredData: List<Article> get() = mSavedDataPerRotation.toList()


    fun saveAsHistory(article: Article) {
        article.type = Article.Type.HISTORY
        Completable.fromAction {
            repo.insert(article)
        }.compose(outsideOfMainThread())
                .subscribe()
    }

    fun saveAsBookmark(article: Article) {
        article.type = Article.Type.BOOKMARK
        Completable.fromAction {
            repo.insert(article)
        }.compose(outsideOfMainThread())
                .subscribe()
    }

    private fun outsideOfMainThread(): CompletableTransformer {
        return CompletableTransformer { upstream -> upstream.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()) }
    }

    private val mFilterList: MutableList<Article> = ArrayList()

    fun addToFilter(article: Article) {
        mFilterList.add(article)
    }

    fun requestData(categoryDescriptor: String) {
        mCompositeDisposable.add(
                retrofit.create(NewsService::class.java)
                        .request(categoryDescriptor, "ru")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { liveNewsResult.value = Outcome.loading(true) }
                        .doOnComplete { liveNewsResult.value = Outcome.loading(false) }
                        .map { it.articles }
                        .flatMap {
                            it.forEach { it.sourceName = it.source?.name ?: "" }
                            Flowable.just(it)
                        }
                        .flatMap {
                            Flowable.just(it.minus(mFilterList))
                        }
                        .flatMap {
                            val outList: MutableList<Article> = ArrayList()
                            it.filter { it.description != null && it.urlToImage != null }.toCollection(outList)
                            Flowable.just(outList)
                        }
                        .doOnNext { it: MutableList<Article> ->
                            if (mSavedDataPerRotation != it) {
                                mSavedDataPerRotation = it
                            }
                        }
                        .subscribe(
                                {
                                    liveNewsResult.value = Outcome.success(it)
                                },
                                {
                                    liveNewsResult.value = Outcome.failure(it)
                                }
                        ))
    }

    fun unsubscribe() {
        mCompositeDisposable.clear()
    }
}