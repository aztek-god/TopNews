package dv.serg.topnews.di.component

import dagger.Component
import dv.serg.lib.dagger.PerActivity
import dv.serg.topnews.di.module.SubSourceModule
import dv.serg.topnews.ui.activity.SubSourceActivity


@Component(dependencies = [AppComponent::class], modules = [SubSourceModule::class])
@PerActivity
interface SubSourceComponent {
    fun inject(activity: SubSourceActivity)
}