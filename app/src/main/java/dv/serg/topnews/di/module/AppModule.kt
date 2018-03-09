package dv.serg.topnews.di.module

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerApplication
import dv.serg.topnews.app.AppContext
import dv.serg.topnews.app.AppDatabase

@Module
@PerApplication
class AppModule(private val appContext: AppContext) {
    @PerApplication
    @Provides
    fun provideAppContext(): Application = appContext


    @PerApplication
    @Provides
    fun provideDatabase(): AppDatabase = Room.databaseBuilder(appContext, AppDatabase::class.java, "app-database").build()
}