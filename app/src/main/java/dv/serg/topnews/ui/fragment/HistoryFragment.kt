package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.lib.android.context.v4.toastShort
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.activity.NavigationActivity
import dv.serg.topnews.ui.holder.HistoryViewHolder
import dv.serg.topnews.ui.viewmodel.RecordViewModel
import dv.serg.topnews.util.openBrowser
import dv.serg.topnews.util.update
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.simple_list_layout.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class HistoryFragment : Fragment() {

    @Inject
    lateinit var vmf: ViewModelProvider.Factory

    private var mAdapter: StandardAdapter<Article, RecordViewModel.RecordViewHolder>? = null

    private val vm: RecordViewModel by lazy {
        val viewModel = ViewModelProviders.of(this, vmf).get(RecordViewModel::class.java)
        viewModel.type = Article.Type.HISTORY
        viewModel
    }

    private var mNotify = true

    private var mFab: FloatingActionButton? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is NavigationActivity) {
            mFab = context.fab
        }

    }

    override fun onDetach() {
        super.onDetach()
        mFab = null
    }

    private var mDeletedIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.injectFragment(this)

        mAdapter = StandardAdapter(R.layout.history_item_layout, { view ->
            val holder = HistoryViewHolder(view = view)

            holder.apply {
                deleteActionListener = {
                    // prevent live data to update list from db
                    mNotify = false

                    mDeletedIndex = it
                    vm.delete(mAdapter!![mDeletedIndex!!])
                    { article ->
                        Snackbar.make(simple_list_view, getString(R.string.snackbar_undo_title), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.undo_snackbar_button_name)) {
                                    mNotify = false
                                    vm.insert(article)
                                    (mAdapter as StandardAdapter<Article, RecordViewModel.RecordViewHolder>).add(mDeletedIndex!!, article)
                                    Observable.timer(1500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                                            .subscribe { toastShort(getString(R.string.toast_data_restored)) }
                                }.show()
                    }
                    (mAdapter as StandardAdapter<*, *>).removeAt(mDeletedIndex!!)

                }
                actionButtonListener = {
                    openBrowser(this@HistoryFragment.context!!, mAdapter!![it].url!!)
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_list_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipe_ref.isEnabled = false

//        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
//        activity?.action
//        mToolbar!!.setDisplayHomeAsUpEnabled
        logd("serg.dv:actionbar = ${activity?.actionBar}")


        vm.mRecordLiveData.getData().observe(this, Observer { data ->
            if (mNotify) {
                mAdapter?.update(data ?: emptyList())
            }
            mNotify = true
        })

        mFab!!.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fr_recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }


    companion object {
        const val TAG = "HistoryFragment"
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}
