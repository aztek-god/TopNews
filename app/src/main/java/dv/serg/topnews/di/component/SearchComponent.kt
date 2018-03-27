package dv.serg.topnews.di.component

import dagger.Component
import dv.serg.lib.dagger.PerActivity
import dv.serg.topnews.di.module.SearchModule
import dv.serg.topnews.ui.activity.SearchActivity

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [SearchModule::class])
interface SearchComponent {
    fun inject(searchActivity: SearchActivity)
}