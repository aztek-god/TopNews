package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.ui.activity.SubSourceActivity
import dv.serg.topnews.ui.holder.SubSourceViewHolder
import dv.serg.topnews.ui.viewmodel.SubSourceViewModel
import kotlinx.android.synthetic.main.simple_list_layout.*
import javax.inject.Inject

class SubscribeFragment : Fragment(), ActionMode.Callback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val vm by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SubSourceViewModel::class.java)
    }

    private var mCompatActivity: AppCompatActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCompatActivity = context as AppCompatActivity
    }


    override fun onDetach() {
        super.onDetach()

        mCompatActivity = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.injectFragment(this)

        if (vm.subSourceAdapter == null) {
            vm.subSourceAdapter = StandardAdapter(R.layout.news_resource_layout, { v ->
                SubSourceViewHolder(v) {
                    if (vm.isActionMode) {
                        if (vm.actionMode == null) {
                            vm.actionMode = mCompatActivity?.startSupportActionMode(this)
                        }
                    } else {
                        vm.actionMode?.finish()
                    }

                    vm.actionMode?.title = "${getString(R.string.action_mode_selected_sources)}: " + vm.selectedCount.toString()
                }
            }).apply {
                addAll(vm.availableSources)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_list_layout, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_ref.isEnabled = false


        fr_recycler.apply {
            adapter = vm.subSourceAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.action_submit -> {
                    val entities = vm.subSourceAdapter?.filter { it.isSelected }
                    entities?.let {
                        vm.save(entities) {
                            vm.subSourceAdapter?.forEach { it.isSelected = false }
                            vm.subSourceAdapter?.notifyDataSetChanged()

                            activity?.setResult(SubSourceActivity.SUBSCRIPTION_RESULT_CODE)
                            activity?.finish()
                            activity?.overridePendingTransition(R.anim.push_in_right_to_left, R.anim.push_out_right_to_left)
                        }
                    }
                    vm.actionMode?.finish()
                }
                else -> {
                }
            }
        }

        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.action_mode_source_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        vm.actionMode?.finish()
        vm.actionMode = null
        vm.subSourceAdapter?.forEach { it.isSelected = false }
        vm.subSourceAdapter?.notifyDataSetChanged()
    }
}