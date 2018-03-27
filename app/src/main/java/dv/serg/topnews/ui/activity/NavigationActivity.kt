package dv.serg.topnews.ui.activity

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import dv.serg.lib.android.context.toastShort
import dv.serg.topnews.R
import dv.serg.topnews.data.SwitchActivity
import dv.serg.topnews.exts.transaction
import dv.serg.topnews.ui.fragment.HotNewsFragment
import dv.serg.topnews.ui.fragment.NewsFragment
import dv.serg.topnews.ui.fragment.RecordFragment
import dv.serg.topnews.ui.view.FabButton
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.content_navigation.*
import java.util.concurrent.TimeUnit

class NavigationActivity : AbstractActivity(), NavigationView.OnNavigationItemSelectedListener, NewsFragment.SearchQueryObservable, SwitchActivity {

    override fun showDataLayout() {
        fr_holder.visibility = View.VISIBLE
        no_internet_error_layout.visibility = View.GONE
    }

    override fun showErrorLayout() {
        fr_holder.visibility = View.GONE
        no_internet_error_layout.visibility = View.VISIBLE
    }

    override var query: String = ""

    enum class ToolbarAction {
        HIDE, SHOW
    }


    private lateinit
    var vm: PersistFragment

    var mToolbar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)


        vm = ViewModelProviders.of(this).get(PersistFragment::class.java)

        mToolbar = supportActionBar


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            currentLayoutId = R.id.news_item
            handleFragment(currentLayoutId)
        }

        vm.toolbarObserver
                .distinctUntilChanged()
                .subscribe {
                    when (it) {
                        ToolbarAction.HIDE -> supportActionBar?.hide()
                        ToolbarAction.SHOW -> supportActionBar?.show()
                        else -> {
                            toastShort("There was failure to try of hiding of action bar.")
                        }
                    }
                }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        vm.currentFabState = news_fab?.visibility ?: View.VISIBLE
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        news_fab?.visibility = vm.currentFabState
    }

    fun fabBtnHide() {
        news_fab.hide()
    }

    fun fabBtnShow() {
        news_fab.show()
    }

    fun hideToolbar() {
        vm.toolbarObserver.onNext(ToolbarAction.HIDE)
    }

    fun showToolbar() {
        vm.toolbarObserver.onNext(ToolbarAction.SHOW)
    }

    fun setFabBtnAction(action: (FabButton) -> Unit = {}) {
        news_fab.setOnClickListener { action.invoke(news_fab) }
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

        when (menuItemRes) {
            R.id.hot_news_item -> {
                toolbar.title = getString(R.string.screen_title_hot_news)
                vm.currentFragment = HotNewsFragment()
            }
            R.id.news_item -> {
                toolbar.title = getString(R.string.screen_title_news)
                vm.currentFragment = NewsFragment()
            }
            R.id.history_item -> {
                toolbar.title = getString(R.string.screen_title_history)
                vm.currentFragment = RecordFragment.newInstance(RecordFragment.Companion.Type.HISTORY)
            }
            R.id.bookmark_item -> {
                toolbar.title = getString(R.string.screen_title_bookmarks)
                vm.currentFragment = RecordFragment.newInstance(RecordFragment.Companion.Type.BOOKMARK)
            }
        }


        supportFragmentManager.transaction { tr -> tr.replace(R.id.fr_holder, vm.currentFragment) }
    }


    class PersistFragment : ViewModel() {
        var currentFragment: Fragment? = null
        var currentFabState: Int = View.VISIBLE

        val toolbarObserver = PublishSubject.create<ToolbarAction>()
    }
}


