package dv.serg.topnews.current

import dagger.Component
import dv.serg.lib.dagger.PerActivity
import dv.serg.topnews.di.component.AppComponent


@Component(dependencies = [AppComponent::class], modules = [SubSourceModule::class])
@PerActivity
interface SubSourceComponent {
    fun inject(activity: SubSourceActivity)
}