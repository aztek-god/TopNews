package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.lib.android.context.v4.toastShort
import dv.serg.lib.collection.StandardAdapter
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


class RecordFragment : Fragment() {

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

    private var mParentActionBar: ActionBar? = null

    private lateinit var mType: Type

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is NavigationActivity) {
            mFab = context.fab
        }
        if (context is NavigationActivity) {
            mParentActionBar = context.mToolbar
        }
    }

    override fun onDetach() {
        super.onDetach()
        mParentActionBar = null
        mFab = null
    }

    private var mDeletedIndex: Int? = null
    private lateinit var mToolbarLabel: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.injectFragment(this)

        mType = Type.values()[arguments?.getInt(TYPE_TAG)!!]

        when (mType) {
            Type.HISTORY -> {
                mToolbarLabel = getString(R.string.history_fragment_label)
                vm.type = Article.Type.HISTORY
            }
            Type.BOOKMARK -> {
                mToolbarLabel = getString(R.string.bookmark_fragment_label)
                vm.type = Article.Type.BOOKMARK
            }
        }

        mParentActionBar?.title = mToolbarLabel

        mAdapter = StandardAdapter(R.layout.record_item_layout, { view ->
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
                    openBrowser(this@RecordFragment.context!!, mAdapter!![it].url!!)
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

        vm.mRecordLiveData.getData().observe(this, Observer { data ->
            if (mNotify) {
                mAdapter?.update(data ?: emptyList())
            }
            mNotify = true
        })

        mFab?.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fr_recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }


    companion object {
        const val TAG = "RecordFragment"

        enum class Type {
            HISTORY, BOOKMARK
        }

        private const val TYPE_TAG = "typeTag"

        fun newInstance(type: Type): RecordFragment {
            return RecordFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE_TAG, type.ordinal)
                }
            }
        }
    }
}
