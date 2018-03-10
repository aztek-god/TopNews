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

class NewsViewModel(private val retrofit: Retrofit, private val subscribeRepo: SubSourceViewModel.Contract.Repository) : ViewModel() {

    private interface NewsService {
        @GET("everything")
        fun request(@Query("sources") sources: String, @Query("page") page: String): Flowable<Response>

        @GET("everything")
        fun request(@Query("sources") sources: String): Flowable<Response>

        @GET("everything")
        fun requestWithQuery(@Query("sources") sources: String, @Query("q") query: String): Flowable<Response>
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val liveNewsResult: MutableLiveData<Outcome<List<Article>>> = MutableLiveData()

    lateinit var standardAdapter: StandardAdapter<Article, NewsViewHolder>

    var mQuery: String = ""

    fun requestData() {
        subscribeRepo.getAll()
                .performOnIoThread()
                .flatMap {
                    val s = it.joinToString(",", transform = { it.code })
                    Flowable.just(if (s.isNotEmpty()) s else "lenta")
                }.subscribe {
                    val sources: String? = it
                    logd("subscribeRepo.getAll:sources = $sources")

                    val response: Flowable<Response> = if (mQuery.isEmpty()) {
                        retrofit.create(NewsService::class.java)
                                .request(sources ?: "lenta")
                    } else {
                        retrofit.create(NewsService::class.java)
                                .requestWithQuery(sources ?: "lenta", mQuery)
                    }

                    logd("NewsViewModel:requestData:response = $response")

                    compositeDisposable.add(
                            response.performOnIoThread()
                                    .doOnSubscribe { liveNewsResult.value = Outcome.loading(true) }
                                    .doOnComplete { liveNewsResult.value = Outcome.loading(false) }
                                    .flatMap {
                                        Flowable.just(it.articles)
                                    }
                                    .subscribe(
                                            {
                                                liveNewsResult.value = Outcome.success(it
                                                        ?: emptyList())
                                            },
                                            {
                                                logd("NewsViewModel:requestData:liveNewsResult.value = $it")
                                                liveNewsResult.value = Outcome.failure(it)
                                            }
                                    ))
                }
    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }
}