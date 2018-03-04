package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.app.performOnIoThread
import dv.serg.topnews.model.Article
import dv.serg.topnews.model.Response
import dv.serg.topnews.ui.holder.NewsViewHolder
import dv.serg.topnews.util.Outcome
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class NewsViewModel(private val retrofit: Retrofit) : ViewModel() {

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


    fun requestData(query: String = "") {
        val response: Flowable<Response> = if (query.isEmpty()) {
            retrofit.create(NewsService::class.java)
                    .request("lenta")
        } else {
            retrofit.create(NewsService::class.java)
                    .requestWithQuery("lenta", query)
        }

        compositeDisposable.add(
                response.performOnIoThread()
                        .doOnSubscribe { liveNewsResult.value = Outcome.loading(true) }
                        .doOnComplete { liveNewsResult.value = Outcome.loading(false) }
                        .cache()
                        .flatMap {
                            Flowable.just(it.articles)
                        }
                        .subscribe(
                                {
                                    liveNewsResult.value = Outcome.success(it ?: emptyList())
                                },
                                {
                                    liveNewsResult.value = Outcome.failure(it)
                                }
                        ))
    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }
}