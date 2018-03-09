package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dv.serg.lib.android.context.v4.toastShort
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.ui.holder.NewsViewHolder
import dv.serg.topnews.ui.viewmodel.NewsViewModel
import dv.serg.topnews.util.Outcome
import dv.serg.topnews.util.SwitchActivity
import dv.serg.topnews.util.update
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject


class NewsFragment : LoggingFragment(), SwipeRefreshLayout.OnRefreshListener {


    @Inject
    lateinit var retrofitViewModelFactory: ViewModelProvider.Factory

    private var queryListener: SearchQueryObservable? = null
    private var switchActivity: SwitchActivity? = null

    private val viewModel: NewsViewModel by lazy {
        ViewModelProviders.of(this, retrofitViewModelFactory).get(NewsViewModel::class.java)
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
            viewModel.standardAdapter = StandardAdapter(R.layout.news_item_layout, { view: View ->
                NewsViewHolder(view) {
                    when (it) {
                        is NewsViewHolder.OpenBrowserException -> {
                            toastShort("${it.message}")
                        }
                        is NewsViewHolder.LoadImageException -> {
                            toastShort("${it.message}")
                        }
                    }
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        fr_news_recycler.adapter = viewModel.standardAdapter
        fr_news_recycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        swipe_news_ref.apply {
            setOnRefreshListener(this@NewsFragment)
        }


        viewModel.liveNewsResult.observe(
                this,
                Observer {
                    when (it) {
                        is Outcome.Success -> {
                            logd("Outcome.Success is loaded. it = ${it}")
                            if (it.type == Outcome.Type.UPDATABLE) {
                                viewModel.standardAdapter.update(it.data)
                            } else if (it.type == Outcome.Type.APPENDABLE) {
                                viewModel.standardAdapter.addAll(it.data)
                            }

                            swipe_news_ref.isRefreshing = false
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
                            toastShort(it.toString())
                        }
                    }
                }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        when (item?.itemId) {
            R.id.action_refresh -> {
                viewModel.requestData(queryListener?.query ?: "")
            }
        }
        return false
    }


    override fun onResume() {
        super.onResume()
//        viewModel.requestData()

        viewModel.requestData(queryListener?.query ?: "")

    }

    override fun onPause() {
        super.onPause()

        // todo implement subscribe/unsubscribe logic
//        viewModel.unsubscribe()
    }

    override fun onRefresh() {
        viewModel.requestData(queryListener?.query ?: "")
    }

    interface SearchQueryObservable {
        var query: String
    }


    companion object {
        private const val QUERY = "query"

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
