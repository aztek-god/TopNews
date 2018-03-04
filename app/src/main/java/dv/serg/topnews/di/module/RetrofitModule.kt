package dv.serg.topnews.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerApplication
import dv.serg.topnews.app.Constants
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RetrofitModule {

    companion object {
        private const val BASE_URL = "https://newsapi.org/v2/"
        private const val HEADER_AUTH = "X-Api-Key"
        private const val CACHE_SIZE: Long = 10 * 1024 * 1024
    }


    @PerApplication
    @Provides
    fun provideCache(appContext: Application): Cache = Cache(appContext.cacheDir, CACHE_SIZE)

    @PerApplication
    @Provides
    fun provideHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestBuilder = originalRequest.newBuilder().header(HEADER_AUTH, Constants.App.API_KEY)
                    val newRequest = requestBuilder.build()
                    chain.proceed(newRequest)
                }
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    @PerApplication
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

}