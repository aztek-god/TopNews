package dv.serg.topnews.ui.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import dv.serg.lib.android.context.v4.toastShort
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import dv.serg.topnews.current.SubSourceActivity
import dv.serg.topnews.di.Injector
import dv.serg.topnews.ui.activity.SearchActivity
import dv.serg.topnews.ui.holder.NewsViewHolder
import dv.serg.topnews.ui.viewmodel.NewsViewModel
import dv.serg.topnews.util.Outcome
import dv.serg.topnews.util.SwitchActivity
import dv.serg.topnews.util.update
import kotlinx.android.synthetic.main.simple_list_layout.*
import javax.inject.Inject


class NewsFragment : LoggingFragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var queryListener: SearchQueryObservable? = null
    private var switchActivity: SwitchActivity? = null

    private val vm: NewsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchQueryObservable) {
            queryListener = context
        }

        if (context is SwitchActivity) {
            switchActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        queryListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.injectFragment(this)
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        if (savedInstanceState == null) {
            vm.standardAdapter = StandardAdapter(R.layout.news_item_layout, { view: View ->
                NewsViewHolder(view) {
                    when (it) {
                        is NewsViewHolder.OpenBrowserException -> {
                            TODO()
                        }
                        is NewsViewHolder.LoadImageException -> {
                            TODO()
                        }
                    }
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.simple_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fr_recycler.adapter = vm.standardAdapter
        fr_recycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        swipe_ref.apply {
            setOnRefreshListener(this@NewsFragment)
        }


        vm.liveNewsResult.observe(
                this,
                Observer {
                    when (it) {
                        is Outcome.Success -> {
                            logd("Outcome.Success is loaded. it = ${it}")
                            if (it.type == Outcome.Type.UPDATABLE) {
                                vm.standardAdapter.update(it.data)
                            } else if (it.type == Outcome.Type.APPENDABLE) {
                                vm.standardAdapter.addAll(it.data)
                            }

                            swipe_ref.isRefreshing = false
                        }
                        is Outcome.Progress -> {
                            if (it.isLoading) {
                                if (it.type == Outcome.Type.UPDATABLE) {
//                                    swipe_news_ref.setProgressViewOffset(true, 0, 800)
//                                    swipe_news_ref.showAtBottomMode(true)
                                }
                            }
                        }
                        is Outcome.Failure -> {
                            logd("Outcome.Failure. it = ${it}")
                            toastShort(it.toString())
                        }
                    }
                }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.navigation_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> {
                startActivityForResult(Intent(activity, SearchActivity::class.java), SearchActivity.SEARCH_QUERY_CODE)
                activity?.overridePendingTransition(R.anim.push_in_right_to_left, R.anim.push_out_right_to_left)
                true
                // todo implement refresh button here
            }
            R.id.action_subscription -> {
                startActivityForResult(Intent(activity, SubSourceActivity::class.java), SubSourceActivity.SUBSCRIPTION_RESULT_CODE)
                activity?.overridePendingTransition(R.anim.push_in_right_to_left, R.anim.push_out_right_to_left)
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


    override fun onResume() {
        super.onResume()
        // todo sort out it
//        vm.requestData()
    }

    override fun onPause() {
        super.onPause()

        // todo implement subscribe/unsubscribe logic
//        vm.unsubscribe()
    }

    override fun onRefresh() {
        vm.requestData()
    }

    interface SearchQueryObservable {
        var query: String
    }


    companion object {
        private const val QUERY = "mQuery"

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
