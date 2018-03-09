package dv.serg.topnews.current

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import dv.serg.lib.android.context.toastShort
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import dv.serg.topnews.R.string.action_mode_selected_sources
import dv.serg.topnews.di.Injector
import kotlinx.android.synthetic.main.activity_subscribe.*
import javax.inject.Inject

class SubSourceActivity : AppCompatActivity(), ActionMode.Callback {

    @Inject
    lateinit var vm: SubSourceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        setSupportActionBar(toolbar)

        // todo in strings.xml
        toolbar.title = "Subscribes"

        Injector.injectSubSourceActivity(this)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (vm.subSourceAdapter == null) {
            vm.subSourceAdapter = StandardAdapter(R.layout.news_resource_layout, { view ->
                SubSourceViewHolder(view) {
                    if (vm.isActionMode) {
                        if (vm.actionMode == null) {
                            vm.actionMode = startSupportActionMode(this)
                        }
                    } else {
                        vm.actionMode?.finish()
                    }

                    vm.actionMode?.title = "${getString(action_mode_selected_sources)}: " + vm.selectedCount.toString()
                }
            }).apply {
                addAll(vm.availableSources)
            }
        }

//        subscribe_recycler_view.apply {
//            adapter = vm.subSourceAdapter
//            layoutManager = LinearLayoutManager(this@SubSourceActivity, LinearLayoutManager.VERTICAL, false)

        // todo for constantly adapter size
//            setHasFixedSize(true)
//        }

//        subscribe_recycler_view.recycledViewPool.getRecycledView(1).
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        logd("actionMode:onActionItemClicked")
        item?.let {
            when (it.itemId) {
                R.id.action_submit -> {
                    val entities = vm.subSourceAdapter?.filter { it.isSelected }
                    entities?.let {
                        vm.save(entities) {
                            vm.run {
                                subSourceAdapter?.forEach { it.isSelected = false }
                                subSourceAdapter?.notifyDataSetChanged()
                            }
                        }
                    }
                    vm.actionMode?.finish()
                }
                else -> {
                    toastShort("There is nothing selected.")
                }
            }
        }

        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        logd("actionMode:onCreateActionMode")
        mode?.menuInflater?.inflate(R.menu.action_mode_source_subscription, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        logd("actionMode:onPrepareActionMode")
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        logd("actionMode:onDestroyActionMode")
        vm.actionMode?.finish()
        vm.actionMode = null
        vm.subSourceAdapter?.forEach { it.isSelected = false }
        vm.subSourceAdapter?.notifyDataSetChanged()
    }
}

