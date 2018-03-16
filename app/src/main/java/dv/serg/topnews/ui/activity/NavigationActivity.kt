package dv.serg.topnews.ui.activity

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import dv.serg.lib.android.context.toastShort
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
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

    var fab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        fab = news_fab

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
        supportFragmentManager.beginTransaction().replace(R.id.fr_holder, NewsFragment()).commit()
//        logd("${hashCode()} handleFragment{menuItemRes = $menuItemRes}")
//        when (menuItemRes) {
//            R.id.hot_news_item -> {
//                val tag = HotNewsFragment::class.java.name
//                var fr: Fragment? = supportFragmentManager.findFragmentByTag(tag)
//                if (fr == null) {
//                    fr = HotNewsFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.fr_holder, fr, tag).commit()
//                } else {
//                    supportFragmentManager.beginTransaction().replace(R.id.fr_holder, fr, tag).commit()
//                }
////                hideFragmentsExceptFor(tag)
//            }
//            R.id.news_item -> {
//                val tag = NewsFragment::class.java.name
//                var fr: Fragment? = supportFragmentManager.findFragmentByTag(tag)
//                if (fr == null) {
//                    fr = NewsFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.fr_holder, fr, tag).commit()
//                } else {
//                    supportFragmentManager.beginTransaction().replace(R.id.fr_holder, fr, tag).commit()
//                }
////                hideFragmentsExceptFor(tag)
//            }
//            R.id.history_item -> {
//                hideFragmentsExceptFor("")
//                supportFragmentManager.beginTransaction().add(R.id.fr_holder, HistoryFragment.newInstance()).commit()
//            }
//            R.id.bookmark_item -> {
//                hideFragmentsExceptFor("")
//                supportFragmentManager.beginTransaction().add(R.id.fr_holder, BookmarkFragment.newInstance()).commit()
//            }
//        }


        logd("supportFragmentManager.fragments = ${supportFragmentManager.fragments}")
    }

    private val tags: List<String> = listOf(HotNewsFragment::class.java.name, NewsFragment::class.java.name)

    private fun hideFragmentsExceptFor(tag: String) {
        tags.filter { it != tag }.forEach {
            val tagFr = supportFragmentManager.findFragmentByTag(it)
            if (tagFr != null) {
                supportFragmentManager.beginTransaction().hide(tagFr).commit()
            }
        }
    }
}
