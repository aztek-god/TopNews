package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import dv.serg.topnews.app.performOnIoThread
import dv.serg.topnews.dao.ArticleContract
import dv.serg.topnews.model.Article
import dv.serg.topnews.model.Response
import dv.serg.topnews.util.Outcome
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.properties.Delegates

class NewsViewModel(private val retrofit: Retrofit, private val subscribeRepo: SubSourceViewModel.Contract.Repository, private val articleRepo: ArticleContract.ArticleDao) : ViewModel() {

    private interface NewsService {
        @GET("everything")
        fun request(@Query("sources") sources: String, @Query("page") page: String): Flowable<Response>

        @GET("everything")
        fun request(@Query("sources") sources: String): Flowable<Response>

        @GET("everything")
        fun requestWithQuery(@Query("sources") sources: String, @Query("q") query: String, @Query("page") page: String): Flowable<Response>
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val liveNewsResult: MutableLiveData<Outcome<List<Article>>> = MutableLiveData()

    private var sources: String? = null

    var isLoading = false

    val filterList: MutableList<Article> = ArrayList()

    private fun getResponse(query: String = "", currentPage: Int = 1): Flowable<Response> {
        return if (query.isEmpty()) {
            retrofit.create(NewsService::class.java).request(sources
                    ?: "lenta", currentPage.toString())
        } else {
            retrofit.create(NewsService::class.java).requestWithQuery(sources
                    ?: "lenta", query, currentPage.toString())
        }
    }

    var currentPage: Int = 1
        set(value) {
            response = getResponse(mQuery, value)
            field = value
        }

    private lateinit var response: Flowable<Response>

    val isSearch: MutableLiveData<Boolean> = MutableLiveData()

    var mQuery: String by Delegates.observable("") { _, oldValue, newValue ->
        if (newValue != oldValue && newValue.isNotEmpty()) {
            response = getResponse(newValue)
        }

        isSearch.value = !newValue.isEmpty()

        if (newValue != oldValue) {
            currentPage = 1
            mQueryChangeListener.invoke(newValue)
        }
    }

    var mQueryChangeListener: (query: String) -> Unit = {}

    var isFirstLaunched = false

    var loadMode: LoadMode = LoadMode.UPDATE

    enum class LoadMode {
        UPDATE, APPEND
    }

    private fun getAllFromDao(): Flowable<String> = subscribeRepo.getAll()
            .performOnIoThread()
            .flatMap {
                val s = it.joinToString(",", transform = { it.code })
                Flowable.just(if (s.isNotEmpty()) s else "lenta")
            }

    private fun getRequestData(loadMode: LoadMode = LoadMode.UPDATE): Flowable<List<Article>?> = response.performOnIoThread()
            .doOnSubscribe { liveNewsResult.value = Outcome.loading(true) }
            .doOnSubscribe { this.loadMode = loadMode }
            .doOnSubscribe { isLoading = true }
            .doOnComplete { liveNewsResult.value = Outcome.loading(false) }
            .doOnComplete { isLoading = false }
            .flatMap { Flowable.just(it.articles) }
            .flatMap {
                val filteredList: List<Article> = it.minus(filterList)
                Flowable.just(filteredList)
            }


    fun requestData(loadMode: LoadMode = LoadMode.UPDATE) {
        if (sources == null) {
            getAllFromDao().doOnNext {
                sources = it
                response = getResponse()
            }.subscribe {
                loadData(loadMode)
            }
        } else {
            loadData(loadMode)
        }
    }

    fun saveAsBookmark(article: Article) {
        Completable.fromAction {
            article.type = Article.Type.BOOKMARK
            articleRepo.insert(article)
        }.performOnIoThread<Unit>().subscribe()
    }


    fun saveAsHistory(article: Article) {
        Completable.fromAction {
            article.type = Article.Type.HISTORY
            articleRepo.insert(article)
        }.performOnIoThread<Unit>().subscribe()
    }


    private var savedDataOnRotationChange: List<Article> = ArrayList()
    val restoreData: List<Article> get() = savedDataOnRotationChange

    private fun loadData(loadMode: LoadMode = LoadMode.UPDATE) {
        compositeDisposable.add(
                getRequestData(loadMode)
                        .subscribe(
                                {
                                    savedDataOnRotationChange = it ?: emptyList()
                                    liveNewsResult.value = Outcome.success(it
                                            ?: emptyList())
                                },
                                {
                                    liveNewsResult.value = Outcome.failure(it)
                                }
                        ))
    }
}