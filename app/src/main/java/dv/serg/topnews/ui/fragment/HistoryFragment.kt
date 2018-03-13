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
import dv.serg.lib.android.context.v4.toastShort
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.holder.HistoryViewHolder
import dv.serg.topnews.ui.viewmodel.RecordViewModel
import dv.serg.topnews.util.update
import kotlinx.android.synthetic.main.fragment_record.*
import javax.inject.Inject


class HistoryFragment : Fragment() {

    @Inject
    lateinit var vmf: ViewModelProvider.Factory

    private val vm: RecordViewModel by lazy {
        val viewModel = ViewModelProviders.of(this, vmf).get(RecordViewModel::class.java)
        viewModel.type = Article.Type.HISTORY
        viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.injectFragment(this)

        if (vm.adapter == null) {
            vm.adapter = StandardAdapter(R.layout.history_item_layout, { view ->
                HistoryViewHolder(view = view, actionButtonCallback = {
                    toastShort("Pressed actionButtonCallback")
                }, actionDeleteCallback = {
                    toastShort("Pressed actionDeleteCallback")
                })
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.articles.observe(this,
                Observer { data ->
                    vm.adapter?.update(data ?: emptyList())
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        record_recycler.apply {
            adapter = vm.adapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }


    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}
