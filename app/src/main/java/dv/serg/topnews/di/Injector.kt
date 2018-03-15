package dv.serg.topnews.di

import dv.serg.topnews.app.AppContext
import dv.serg.topnews.di.component.AppComponent
import dv.serg.topnews.di.component.DaggerAppComponent
import dv.serg.topnews.di.component.DaggerFragmentComponent
import dv.serg.topnews.di.component.DaggerSubSourceComponent
import dv.serg.topnews.di.module.AppModule
import dv.serg.topnews.di.module.FragmentModule
import dv.serg.topnews.ui.activity.SubSourceActivity
import dv.serg.topnews.ui.fragment.*


object Injector {
    fun getAppComponent(appContext: AppContext): AppComponent {
        return DaggerAppComponent.builder().appModule(AppModule(appContext)).build()
    }

    fun injectFragment(fragment: InfoFragment) {
        DaggerFragmentComponent.builder().appComponent(AppContext.appComponent)
                .fragmentModule(FragmentModule()).build().inject(fragment)
    }

    fun injectFragment(fragment: NewsFragment) {
        DaggerFragmentComponent.builder().appComponent(AppContext.appComponent)
                .fragmentModule(FragmentModule()).build().inject(fragment)
    }

    fun injectFragment(fragment: SubscribeFragment) {
        DaggerFragmentComponent.builder().appComponent(AppContext.appComponent)
                .fragmentModule(FragmentModule()).build().inject(fragment)
    }

    fun injectFragment(fragment: HistoryFragment) {
        DaggerFragmentComponent.builder().appComponent(AppContext.appComponent)
                .fragmentModule(FragmentModule()).build().inject(fragment)
    }

    fun injectFragment(fragment: BookmarkFragment) {
        DaggerFragmentComponent.builder().appComponent(AppContext.appComponent)
                .fragmentModule(FragmentModule()).build().inject(fragment)
    }

    fun injectSubSourceActivity(activity: SubSourceActivity) {
        DaggerSubSourceComponent.builder().appComponent(AppContext.appComponent).build().inject(activity)
    }
}