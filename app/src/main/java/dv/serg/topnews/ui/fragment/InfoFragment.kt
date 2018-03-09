package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
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
import dv.serg.topnews.app.AppContext
import dv.serg.topnews.app.Constants
import dv.serg.topnews.di.Injector
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.holder.HotNewsHolder
import dv.serg.topnews.ui.viewmodel.HotNewsViewModel
import dv.serg.topnews.util.ObservableProperty
import kotlinx.android.synthetic.main.fragment_info.*
import javax.inject.Inject

class InfoFragment : LoggingFragment(), SwipeRefreshLayout.OnRefreshListener {

    private var mSourceType: String? = null

    // todo sort out this
    private var isRefreshing = false

    private val viewModel by lazy {
        ViewModelProviders.of(this, retrofitViewModelFactory).get(HotNewsViewModel::class.java)
    }

    @Inject
    lateinit var retrofitViewModelFactory: ViewModelProvider.Factory

    private val propertyObserver: ObservableProperty<Constants.RequestState> = ObservableProperty(Constants.RequestState.IDLE).also { it ->
        it.registerAsObserver(object : ObservableProperty.PropertyObserver<Constants.RequestState> {
            override fun observe(value: Constants.RequestState) {
                when (value) {
                    Constants.RequestState.IDLE -> {
                        doOnIdle()
                    }
                    Constants.RequestState.LOADING -> {
                        doOnLoading()
                    }
                    Constants.RequestState.COMPLETE -> {
                        doOnComplete()
                    }
                    Constants.RequestState.ERROR -> {
                        doOnError()
                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mSourceType = arguments!!.getString(FRAGMENT_NAME)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_ref.apply {
            setOnRefreshListener(this@InfoFragment)
        }

        swipe_ref.setColorSchemeColors(ContextCompat.getColor(AppContext.appContext, R.color.colorAccent))

        fr_recycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Injector.injectFragment(this)

        if (savedInstanceState == null) {
            viewModel.standardAdapter = StandardAdapter(R.layout.hot_news_item_layout, { e: View -> HotNewsHolder(e) })
            viewModel.propertyObserver = propertyObserver
        }

        fr_recycler.adapter = viewModel.standardAdapter

        viewModel.liveNewsResult.observe(this,
                Observer { it: List<Article>? ->
                    viewModel.standardAdapter.addAll(it ?: emptyList())
                }
        )

        viewModel.liveNewsErrors.observe(this,
                Observer {
                    toastShort("Error occured")
                    propertyObserver.updateValue(Constants.RequestState.ERROR)
                }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_refresh -> {
                requestData()
                logd("onOptionsItemSelected")
            }
        }
        return false
    }


    override fun onResume() {
        super.onResume()
        requestData()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unsubscribe()
    }

    override fun onRefresh() {
        checkNotNull(mSourceType)
        viewModel.requestData(mSourceType!!)
    }

    private fun requestData() {
        isRefreshing = true
        viewModel.requestData(mSourceType ?: "")
    }

    private fun doOnIdle() {
        swipe_ref.isRefreshing = false
    }

    private fun doOnLoading() {
        swipe_ref.isRefreshing = true
    }

    private fun doOnError() {
        swipe_ref.isRefreshing = false
    }

    private fun doOnComplete() {
        swipe_ref.isRefreshing = false
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
