package dv.serg.topnews.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dagger.PerFragment
import dv.serg.topnews.ui.viewmodel.ViewModelFactory
import retrofit2.Retrofit

@Module
@PerFragment
class FragmentModule {
    @PerFragment
    @Provides
    fun provideViewModelFactory(retrofit: Retrofit): ViewModelFactory {
        return ViewModelFactory(retrofit)
    }
}