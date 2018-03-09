package dv.serg.topnews.di.component

import android.app.Application
import dagger.Component
import dv.serg.lib.dagger.PerApplication
import dv.serg.topnews.app.AppDatabase
import dv.serg.topnews.di.module.AppModule
import dv.serg.topnews.di.module.RetrofitModule
import retrofit2.Retrofit

@PerApplication
@Component(modules = [AppModule::class, RetrofitModule::class])
interface AppComponent {
    fun appContext(): Application
    fun retrofit(): Retrofit
    fun appDatabase(): AppDatabase
}