package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.lib.android.context.v4.toastShort
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.ui.holder.NewsViewHolder
import dv.serg.topnews.ui.viewmodel.NewsViewModel
import dv.serg.topnews.ui.viewmodel.ViewModelFactory
import dv.serg.topnews.util.SwitchActivity
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject


class NewsFragment : StatefulFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var queryListener: SearchQueryObservable? = null
    private var switchActivity: SwitchActivity? = null

    private val viewModel: NewsViewModel by lazy {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Injector.injectFragment(this)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fr_news_recycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        savedInstanceState?.let {
            // todo for feature purposes
        } ?: run {
            StandardAdapter(R.layout.news_item_layout, { view: View ->
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
            }).apply {
                fr_news_recycler.adapter = this
                viewModel.standardAdapter = this
            }
        }

        viewModel.state = state

        viewModel.liveNewsResult.observe(
                this,
                Observer {
                    viewModel.standardAdapter.addAll(it ?: emptyList())
                }
        )
        viewModel.liveNewsErrors.observe(
                this,
                Observer {

                }
        )
    }

    override fun onResume() {
        super.onResume()
//        viewModel.requestData()

        viewModel.requestData(queryListener?.query ?: "")

    }

    override fun onPause() {
        super.onPause()
//        viewModel.unsubscribe()
    }

    interface SearchQueryObservable {
        var query: String
    }

    override fun onLoading() {
        swipe_news_ref.isRefreshing = true
    }

    override fun onIdle() {
        swipe_news_ref.isRefreshing = false
    }

    override fun onComplete() {
        swipe_news_ref.isRefreshing = false
    }

    override fun onError() {
        switchActivity?.showErrorLayout()
        swipe_news_ref.isRefreshing = false
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
