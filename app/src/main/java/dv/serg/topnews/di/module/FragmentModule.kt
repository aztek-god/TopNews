package dv.serg.topnews.di.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerFragment
import dv.serg.topnews.ui.viewmodel.RetrofitViewModelFactory
import retrofit2.Retrofit

@Module
@PerFragment
class FragmentModule {
    @PerFragment
    @Provides
    fun provideViewModelFactory(retrofit: Retrofit): ViewModelProvider.Factory {
        return RetrofitViewModelFactory(retrofit)
    }
}