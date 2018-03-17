package dv.serg.topnews.ui.fragment

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import dv.serg.lib.android.context.v4.toastShort
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.PaginationScrollListener
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.activity.NavigationActivity
import dv.serg.topnews.ui.activity.SearchActivity
import dv.serg.topnews.ui.activity.SubSourceActivity
import dv.serg.topnews.ui.holder.NewsViewHolder
import dv.serg.topnews.ui.viewmodel.NewsViewModel
import dv.serg.topnews.util.Outcome
import dv.serg.topnews.util.SwitchActivity
import dv.serg.topnews.util.openBrowser
import dv.serg.topnews.util.update
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.list_state_layout.*
import kotlinx.android.synthetic.main.no_internet_connection_layout.*
import kotlinx.android.synthetic.main.simple_list_layout.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class NewsFragment : LoggingFragment(), SwipeRefreshLayout.OnRefreshListener {
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private var mQueryListener: SearchQueryObservable? = null
    private var mSwitchActivity: SwitchActivity? = null

    private val vm: NewsViewModel by lazy {
        ViewModelProviders.of(this, mViewModelFactory).get(NewsViewModel::class.java)
    }

    private var mOwnerActivity: LifecycleOwner? = null

    private var mParentActivity: NavigationActivity? = null

    private var mContext: Context? = null

    private var mfab: FloatingActionButton? = null

    private lateinit var mAdapter: StandardAdapter<Article, NewsViewHolder>


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mContext = context
        if (context is SearchQueryObservable) {
            mQueryListener = context
        }

        if (context is SwitchActivity) {
            mSwitchActivity = context
        }

        if (context is NavigationActivity) {
            mfab = context.fab
        }

        mOwnerActivity = context as AppCompatActivity

    }

    override fun onDetach() {
        super.onDetach()
        mQueryListener = null
        mOwnerActivity = null
        mfab = null
        mContext = null
        mParentActivity = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.injectFragment(this)
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.list_state_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAdapter = StandardAdapter(R.layout.news_item_layout, { v: View ->
            NewsViewHolder(v) {
                when (it) {
                    is NewsViewHolder.OpenBrowserException -> {
                    }
                    is NewsViewHolder.LoadImageException -> {
                    }
                }
            }.apply {
                fm = childFragmentManager
            }.apply {
                addToFilterAction = { item ->
                    vm.filterList.add(item)
                }
            }.apply {
                addToBookmarkAction = { item ->
                    vm.saveAsBookmark(item)
                }
            }.apply {
                shortClickListener = {
                    if (it.url == null) {
                        Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { toastShort("Page temporarily unavailable.") }
                    } else {
                        openBrowser(context!!, it.url!!)
                        vm.saveAsHistory(it)
                    }
                }
            }
        }).also {
            fr_recycler.apply {
                adapter = it
            }.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }.apply {
                addOnScrollListener(
                        object : PaginationScrollListener(fr_recycler.layoutManager as LinearLayoutManager) {
                            override fun getTotalPageCount(): Int = mAdapter.size

                            override fun isLastPage(): Boolean {
                                return false
                            }

                            override fun isLoading(): Boolean {
                                return vm.isLoading
                            }

                            override fun loadMoreItems() {
                                vm.currentPage += 1
                                vm.requestData(NewsViewModel.LoadMode.APPEND)
                            }
                        }

                )
            }
        }

        swipe_ref.apply {
            setOnRefreshListener(this@NewsFragment)
        }

        retry.setOnClickListener {
            vm.requestData()
        }

    }

    private fun resetPagination() {
        vm.currentPage = 1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mfab?.setOnClickListener {
            vm.mQuery = ""
            vm.requestData()
        }

        if (vm.mQuery.isEmpty()) {
            mfab?.hide()
        } else {
            mfab?.show()
        }

        vm.mQueryChangeListener = {
            if (it.isEmpty()) {
                mfab?.hide()
            } else {
                mfab?.show()
            }
        }



        vm.liveNewsResult.observe(
                this,
                Observer {
                    when (it) {
                        is Outcome.Success -> {
                            showListLayout()
                            if (vm.loadMode == NewsViewModel.LoadMode.UPDATE) {
                                mAdapter.update(it.data)
                            } else {
                                mAdapter.addAll(it.data)
                            }
                        }
                        is Outcome.Progress -> {
                            swipe_ref.isRefreshing = it.isLoading
                        }
                        is Outcome.Failure -> {
                            showErrorLayout()
                            toastShort(getString(R.string.network_error))
                        }
                    }
                }
        )

        vm.isSearch.observe(this, Observer {
            if (vm.isSearch.value == true) {
                mParentActivity?.fab?.show()
            } else {
                mParentActivity?.fab?.hide()
            }
        })

        if (!vm.isFirstLaunched) {
            vm.isFirstLaunched = true
            vm.requestData()
        } else {
            mAdapter.update(vm.restoreData)
            showData()
        }
    }


    private fun showData() {
        showListLayout()
        fr_recycler.adapter = mAdapter
    }

    private fun showErrorLayout() {
        layout_error.visibility = View.VISIBLE
        layout_list.visibility = View.GONE
    }

    private fun showListLayout() {
        layout_error.visibility = View.GONE
        layout_list.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.navigation_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> {
                startActivityForResult(Intent(activity, SearchActivity::class.java), SearchActivity.SEARCH_QUERY_CODE)
                activity?.overridePendingTransition(R.anim.push_in_right_to_left, R.anim.push_out_right_to_left)

                resetPagination()
                true
            }
            R.id.action_subscription -> {
                startActivityForResult(Intent(activity, SubSourceActivity::class.java), SubSourceActivity.SUBSCRIPTION_RESULT_CODE)
                activity?.overridePendingTransition(R.anim.push_in_right_to_left, R.anim.push_out_right_to_left)
                true
            }
            R.id.action_refresh -> {
                vm.requestData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SearchActivity.SEARCH_QUERY_CODE) {
                val searchQuery = data?.getStringExtra(SearchActivity.SEARCH_QUERY)
                if (vm.mQuery != searchQuery) {
                    vm.mQuery = searchQuery ?: ""
                    vm.requestData()
                }
            }
            if (requestCode == SubSourceActivity.SUBSCRIPTION_RESULT_CODE) {
                vm.mQuery = ""
                vm.requestData()
            }
        }
    }

    override fun onRefresh() {
        resetPagination()
        vm.requestData()
    }

    interface SearchQueryObservable {
        var query: String
    }


    companion object {
        private const val QUERY = "mQuery"
        const val TAG = "NewsFragment"

        fun newInstance(query: String = ""): NewsFragment {
            val instance = NewsFragment()
            val bundle = Bundle().apply {
                putString(QUERY, query)
            }

            instance.arguments = bundle

            return instance
        }
    }
}
