package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.exts.update
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.holder.RecordViewHolder
import dv.serg.topnews.ui.viewmodel.RecordViewModel
import kotlinx.android.synthetic.main.simple_list_layout.*
import javax.inject.Inject

class BookmarkFragment : Fragment() {

    @Inject
    lateinit var vmf: ViewModelProvider.Factory

    private val vm: RecordViewModel by lazy {
        val viewModel = ViewModelProviders.of(this, vmf).get(RecordViewModel::class.java)
        viewModel.type = Article.Type.BOOKMARK
        viewModel
    }

    private var mAdapter: StandardAdapter<Article, RecordViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.injectFragment(this)

        if (mAdapter == null) {
            mAdapter = StandardAdapter(R.layout.record_item_layout, { view ->
                RecordViewHolder(view = view)
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.list_state_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.articles.observe(this,
                Observer { data ->
                    mAdapter?.update(data ?: emptyList())
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fr_recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }


    companion object {
        fun newInstance(): BookmarkFragment {
            return BookmarkFragment()
        }
    }

}
