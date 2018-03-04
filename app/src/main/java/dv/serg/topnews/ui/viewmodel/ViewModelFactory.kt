package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import retrofit2.Retrofit

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val retrofit: Retrofit) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(HotNewsViewModel::class.java) -> HotNewsViewModel(retrofit)
                    isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(retrofit)
                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
}