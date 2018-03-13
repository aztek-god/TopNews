package dv.serg.topnews.ui.activity

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import dv.serg.lib.android.context.toastShort
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import dv.serg.topnews.ui.fragment.BookmarkFragment
import dv.serg.topnews.ui.fragment.HistoryFragment
import dv.serg.topnews.ui.fragment.HotNewsFragment
import dv.serg.topnews.ui.fragment.NewsFragment
import dv.serg.topnews.util.SwitchActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.content_navigation.*
import java.util.concurrent.TimeUnit

class NavigationActivity : LoggingActivity(), NavigationView.OnNavigationItemSelectedListener, NewsFragment.SearchQueryObservable, SwitchActivity {

    override fun showDataLayout() {
        fr_holder.visibility = View.VISIBLE
        no_internet_error_layout.visibility = View.GONE
    }

    override fun showErrorLayout() {
        fr_holder.visibility = View.GONE
        no_internet_error_layout.visibility = View.VISIBLE
    }

    override var query: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            currentLayoutId = R.id.news_item
            handleFragment(currentLayoutId)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        if (doOnBackPressed()) {
            super.onBackPressed()
        }
    }

    private var isBackPressed = false

    private fun doOnBackPressed(): Boolean {
        if (isBackPressed) {
            return true
        }

        isBackPressed = true

        toastShort(getString(R.string.toast_button_exit_message))
        Observable.timer(2, TimeUnit.SECONDS).subscribe { isBackPressed = false }

        return false
    }

    private var currentLayoutId: Int = R.id.hot_news_item

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (currentLayoutId != item.itemId) {
            currentLayoutId = item.itemId
            handleFragment(item.itemId)
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleFragment(@IdRes menuItemRes: Int) {

        logd("${hashCode()} handleFragment{menuItemRes = $menuItemRes}")

        var fr: Fragment? = null

        when (menuItemRes) {
            R.id.hot_news_item -> {
                fr = HotNewsFragment.newInstance()
            }
            R.id.news_item -> {
                fr = NewsFragment.newInstance()
            }
            R.id.history_item -> {
                fr = HistoryFragment()
            }
            R.id.bookmark_item -> {
                fr = BookmarkFragment()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fr_holder, fr, fr!!.javaClass.name.toString()).commit()

//        supportFragmentManager.fragments.forEach {
//            if(fr != it) {
//                supportFragmentManager.beginTransaction().hide(fr).commit()
//            }
//        }

        logd("supportFragmentManager.fragments = ${supportFragmentManager.fragments}")
    }

    fun getFragmentByTag(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }


}
