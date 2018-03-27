package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.app.AppContext
import dv.serg.topnews.data.Outcome
import dv.serg.topnews.di.Injector
import dv.serg.topnews.exts.isNetworkConnected
import dv.serg.topnews.exts.openBrowser
import dv.serg.topnews.exts.setColor
import dv.serg.topnews.exts.update
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.activity.NavigationActivity
import dv.serg.topnews.ui.holder.HotNewsHolder
import dv.serg.topnews.ui.viewmodel.HotNewsViewModel
import kotlinx.android.synthetic.main.empty_layout.*
import kotlinx.android.synthetic.main.misc_layout.*
import kotlinx.android.synthetic.main.no_internet_connection_layout.*
import kotlinx.android.synthetic.main.simple_list_layout.*
import javax.inject.Inject

class InfoFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var mSourceType: String? = null

    private var isRefreshing = false

    private val vm by lazy {
        ViewModelProviders.of(this, retrofitViewModelFactory).get(HotNewsViewModel::class.java)
    }

    @Inject
    lateinit var retrofitViewModelFactory: ViewModelProvider.Factory

    private lateinit var mAdapter: StandardAdapter<Article, HotNewsHolder>

    private var parentOwner: LifecycleOwner? = null

    private var mParentActivity: NavigationActivity? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        parentOwner = context as LifecycleOwner
        mParentActivity = context as NavigationActivity
    }

    override fun onDetach() {
        super.onDetach()
        parentOwner = null
        mParentActivity = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.injectFragment(this)

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mSourceType = arguments?.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)

        mAdapter = StandardAdapter(R.layout.hot_news_item_layout, { e: View ->
            val receiver = HotNewsHolder(e)
            with(receiver) {
                apply { fm = childFragmentManager }
                apply {
                    addToFilterAction = { item ->
                        vm.addToFilter(item)
                        mAdapter.remove(item)
                    }
                }
                apply {
                    clickListener = {
                        openBrowser(this@InfoFragment.context ?: AppContext.appContext, it.url
                                ?: "")
                        vm.saveAsHistory(it)
                    }
                }
                apply {
                    addToBookmarkAction = {
                        vm.saveAsBookmark(it)
                    }
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.misc_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_ref.apply {
            setOnRefreshListener(this@InfoFragment)
        }

        swipe_ref.setColorSchemeColors(ContextCompat.getColor(AppContext.appContext, R.color.colorAccent))

        with(fr_recycler) {
            apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
            apply { adapter = mAdapter }
        }

        retry.setOnClickListener { vm.requestData(mSourceType ?: "") }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showListLayout()

        if (vm.isFirstLaunched || mAdapter.isEmpty()) {
            requestData()
            vm.isFirstLaunched = false
        } else {
            mAdapter.update(vm.restoredData)
        }


        vm.output.observe(
                this, Observer {
            when (it) {
                is Outcome.Success -> {
                    mParentActivity?.showToolbar()

                    if (it.data.isEmpty()) {
                        showEmptyLayout()
                    } else {
                        showListLayout()
                    }

                    if (!mAdapter.containsAll(it.data)) {
                        mAdapter.update(it.data)
                    }
                }
                is Outcome.Failure -> {
                    context?.let {
                        if (context?.isNetworkConnected() != true) {
                            mParentActivity?.hideToolbar()
                            showErrorLayout()
                        }
                    }
                }
                is Outcome.Progress -> {
                    swipe_ref.isRefreshing = it.isLoading
                }
            }
        }
        )
    }

    private fun showListLayout() {
        if (error_simple_simple_list.visibility != View.VISIBLE) {
            error_simple_no_internet_connection.visibility = View.GONE
            error_simple_simple_list.visibility = View.VISIBLE
            error_simple_empty_layout.visibility = View.GONE
        }
    }

    private fun showErrorLayout() {
        if (error_simple_no_internet_connection.visibility != View.VISIBLE) {
            error_simple_no_internet_connection.visibility = View.VISIBLE
            error_simple_simple_list.visibility = View.GONE
            error_simple_empty_layout.visibility = View.GONE
        }
    }

    private fun showEmptyLayout() {
        if (error_simple_empty_layout.visibility != View.VISIBLE) {
            endStackImg.setColor(R.color.colorAccent)
            error_simple_no_internet_connection.visibility = View.GONE
            error_simple_simple_list.visibility = View.GONE
            error_simple_empty_layout.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.info_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.info_refresh -> {
                requestData()
            }
            android.R.id.home -> {
            }
        }
        return false
    }


    override fun onPause() {
        super.onPause()
        vm.unsubscribe()
    }

    override fun onRefresh() {
        checkNotNull(mSourceType)
        vm.requestData(mSourceType ?: "")
    }

    private fun requestData() {
        isRefreshing = true
        vm.requestData(mSourceType ?: "")
    }


    companion object {
        private const val FRAGMENT_NAME = "frag_name"

        fun newInstance(frName: String): InfoFragment {
            val fragment = InfoFragment()
            val args = Bundle()
            args.putString(FRAGMENT_NAME, frName)
            fragment.arguments = args
            return fragment
        }
    }
}
