package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.logd
import dv.serg.topnews.app.performOnIoThread
import dv.serg.topnews.current.SubSourceViewModel
import dv.serg.topnews.model.Article
import dv.serg.topnews.model.Response
import dv.serg.topnews.ui.holder.NewsViewHolder
import dv.serg.topnews.util.Outcome
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.properties.Delegates

class NewsViewModel(private val retrofit: Retrofit, private val subscribeRepo: SubSourceViewModel.Contract.Repository) : ViewModel() {

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

    var standardAdapter: StandardAdapter<Article, NewsViewHolder>? = null

    private var sources: String? = null

    var isLoading = false

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

    private var response: Flowable<Response> = getResponse()

    var mQuery: String by Delegates.observable("") { property, oldValue, newValue ->
        if (newValue != oldValue && newValue.isNotEmpty()) {
            response = getResponse(newValue)
        }

        if (newValue != oldValue) {
            currentPage = 1
        }
    }

    var isFirstLaunched = false

    var loadMode: LoadMode = LoadMode.UPDATE

    enum class LoadMode {
        UPDATE, APPEND
    }

    private val getAllFromDao: Flowable<String> = subscribeRepo.getAll()
            .performOnIoThread()
            .flatMap {
                val s = it.joinToString(",", transform = { it.code })
                Flowable.just(if (s.isNotEmpty()) s else "lenta")
            }

    private fun getRequestData(loadMode: LoadMode = LoadMode.UPDATE): Flowable<List<Article>?> = response.performOnIoThread()
            .doOnSubscribe { liveNewsResult.value = Outcome.loading(true) }
            .doOnSubscribe { this.loadMode = loadMode }
            .doOnSubscribe {
                isLoading = true
            }
            .doOnComplete { liveNewsResult.value = Outcome.loading(false) }
            .doOnComplete { isLoading = false }
            .flatMap {
                Flowable.just(it.articles)
            }


    fun requestData(loadMode: LoadMode = LoadMode.UPDATE) {
        logd("requestData with = $currentPage")
        if (sources == null) {
            getAllFromDao.doOnNext {
                sources = it
            }.subscribe {
                loadData(loadMode)
            }
        } else {
            loadData(loadMode)
        }
    }

    private fun loadData(loadMode: LoadMode = LoadMode.UPDATE) {
        compositeDisposable.add(
                getRequestData(loadMode)
                        .subscribe(
                                {
                                    liveNewsResult.value = Outcome.success(it
                                            ?: emptyList())
                                },
                                {
                                    liveNewsResult.value = Outcome.failure(it)
                                }
                        ))
    }
}