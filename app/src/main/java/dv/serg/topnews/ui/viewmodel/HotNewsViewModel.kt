package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.app.Constants
import dv.serg.topnews.extension.toLiveData
import dv.serg.topnews.model.Article
import dv.serg.topnews.model.Response
import dv.serg.topnews.ui.holder.HotNewsHolder
import dv.serg.topnews.util.ObservableProperty
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class HotNewsViewModel(private val retrofit: Retrofit) : ViewModel() {

    private interface NewsService {
        @GET("top-headlines")
        fun request(@Query("category") category: String, @Query("country") country: String): Flowable<Response>
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val newsOutcome: PublishSubject<List<Article>> = PublishSubject.create()
    private val newsErrorOutcome: PublishSubject<Throwable> = PublishSubject.create()


    val liveNewsResult: LiveData<List<Article>> get() = newsOutcome.toLiveData(compositeDisposable)
    val liveNewsErrors: LiveData<Throwable> get() = newsErrorOutcome.toLiveData(compositeDisposable)

    lateinit var propertyObserver: ObservableProperty<Constants.RequestState>

    lateinit var standardAdapter: StandardAdapter<Article, HotNewsHolder>

    fun requestData(categoryDescriptor: String) {
        compositeDisposable.add(
                retrofit.create(NewsService::class.java)
                        .request(categoryDescriptor, "ru")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            propertyObserver.updateValue(Constants.RequestState.LOADING)
                        }
                        .flatMap {
                            val outList: MutableList<Article> = ArrayList()
                            it.articles?.filter { it.description != null && it.urlToImage != null }?.toCollection(outList)
                            Flowable.just(outList)
                        }
                        .cache()
                        .doOnError {
                            propertyObserver.updateValue(Constants.RequestState.ERROR)
                        }.doOnComplete {
                            propertyObserver.updateValue(Constants.RequestState.COMPLETE)
                        }
                        .subscribe(
                                {
                                    newsOutcome.onNext(it ?: emptyList())
                                },
                                {
                                    newsErrorOutcome.onNext(it)
                                }
                        ))
    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }
}