package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.app.Constants
import dv.serg.topnews.app.performOnIoThread
import dv.serg.topnews.model.Article
import dv.serg.topnews.model.Response
import dv.serg.topnews.ui.holder.NewsViewHolder
import dv.serg.topnews.util.ObservableProperty
import dv.serg.topnews.util.StatefulComponent
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

    val liveNewsResult: MutableLiveData<List<Article>> = MutableLiveData()
    val liveNewsErrors: MutableLiveData<Throwable> = MutableLiveData()

    lateinit var propertyObserver: ObservableProperty<Constants.RequestState>
    lateinit var standardAdapter: StandardAdapter<Article, NewsViewHolder>
    lateinit var state: StatefulComponent.State

    fun requestData(query: String = "") {
        val response: Flowable<Response> = if (query.isEmpty()) {
            retrofit.create(NewsService::class.java)
                    .request("lenta")
        } else {
            standardAdapter.clear()
            retrofit.create(NewsService::class.java)
                    .requestWithQuery("lenta", query)
        }

        compositeDisposable.add(
                response.performOnIoThread()
                        .doOnSubscribe { state = StatefulComponent.State.LOADING }
                        .doOnComplete { state = StatefulComponent.State.COMPLETE }
                        .doOnError { state = StatefulComponent.State.ERROR }
                        .cache()
                        .flatMap {
                            Flowable.just(it.articles)
                        }
                        .subscribe(
                                {
                                    liveNewsResult.value = it
                                },
                                {
                                    liveNewsErrors.value = it
                                }
                        ))
    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }
}