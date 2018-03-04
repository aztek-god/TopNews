package dv.serg.topnews.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerApplication
import dv.serg.topnews.app.AppContext

@Module
@PerApplication
class AppModule(private val appContext: AppContext) {
    @PerApplication
    @Provides
    fun provideAppContext(): Application = appContext
}