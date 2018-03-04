package dv.serg.topnews.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import dv.serg.topnews.ui.fragment.HotNewsFragment
import dv.serg.topnews.ui.fragment.NewsFragment
import dv.serg.topnews.util.SwitchActivity
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.content_navigation.*

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
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            R.id.action_search -> {
                startActivityForResult(Intent(this, SearchActivity::class.java), SearchActivity.SEARCH_QUERY_CODE)
                overridePendingTransition(R.anim.push_in_right_to_left, R.anim.push_out_right_to_left)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SearchActivity.SEARCH_QUERY_CODE) {
                query = data?.getStringExtra(SearchActivity.SEARCH_QUERY) ?: ""
            }
        }

        data?.let {
            currentLayoutId = R.id.news_item
            handleFragment(currentLayoutId)
        }
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
        var tag: String? = null

        when (menuItemRes) {
            R.id.hot_news_item -> {
                tag = HotNewsFragment::class.java.name
                fr = getFragmentByTag(tag)
                if (fr == null) {
                    fr = HotNewsFragment.newInstance()
                }
            }
            R.id.news_item -> {
                tag = NewsFragment::class.java.name
                fr = getFragmentByTag(tag)
                if (fr == null) {
                    fr = NewsFragment.newInstance()
                }
            }
            R.id.history_item -> {

            }
            R.id.bookmark_item -> {

            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fr_holder, fr, tag).commit()
    }

    fun getFragmentByTag(tag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(tag)
    }


}
