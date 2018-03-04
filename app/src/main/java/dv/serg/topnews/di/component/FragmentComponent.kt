package dv.serg.topnews.di.component

import dagger.Component
import dv.serg.lib.dagger.PerFragment
import dv.serg.topnews.di.module.FragmentModule
import dv.serg.topnews.ui.fragment.InfoFragment
import dv.serg.topnews.ui.fragment.NewsFragment

@PerFragment
@Component(dependencies = [AppComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: InfoFragment)
    fun inject(fragment: NewsFragment)
}