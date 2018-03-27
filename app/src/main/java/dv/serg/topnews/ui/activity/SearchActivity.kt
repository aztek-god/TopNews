package dv.serg.topnews.ui.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageButton
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.di.Injector
import dv.serg.topnews.exts.alert
import dv.serg.topnews.exts.update
import dv.serg.topnews.model.Suggestion
import dv.serg.topnews.ui.holder.SearchHolder
import dv.serg.topnews.ui.view.PrettySearchView
import dv.serg.topnews.ui.viewmodel.SearchViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.search_primary_row_layout.*
import kotlinx.android.synthetic.main.simple_list_layout.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates

class SearchActivity : AbstractActivity() {

    private lateinit var closeBtn: ImageButton
    private var searchQuery: String by Delegates.observable("") { _, _, newValue ->
        if (newValue.isEmpty()) {
            closeBtn.visibility = View.GONE
        } else {
            closeBtn.visibility = View.VISIBLE
        }
    }

    @Inject
    lateinit var vm: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Injector.inject(this)

        swipe_ref.isEnabled = false

        nothing_search_id.visibility = View.VISIBLE
        search_layout_id.visibility = View.GONE

        closeBtn = search_view.findViewById<ImageButton>(R.id.close_action).apply {
            visibility = View.GONE
        }

        search_view.setOnSearchListener(object : PrettySearchView.OnSearchListener {
            override fun onQueryChange(query: String) {
                searchQuery = query
            }

            override fun onSearchAction(currentQuery: String) {
                searchQuery = currentQuery

                if (searchQuery.isNotEmpty()) {
                    vm.saveSuggestion(Suggestion(searchQuery))
                }

                val resultIntent: Intent = Intent().apply {
                    putExtra(SEARCH_QUERY, searchQuery)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                enterTransition()
                return
            }

            override fun onLeftAction(view: View, searchView: PrettySearchView) {
                finish()
                overridePendingTransition(R.anim.push_in_left_to_right, R.anim.push_out_left_to_right)
            }

            override fun onRightAction(view: View, searchView: PrettySearchView) {
                search_view.clear()
            }
        })

        search_view.setHint(getString(R.string.search_view_hint))

        val standardAdapter = StandardAdapter(
                R.layout.search_result_row_layout, { v: View ->
            SearchHolder(v).apply {
                onClickListener = { _, suggestion -> search_view.setQuery(suggestion.suggestionQuery) }
            }
        })

        fr_recycler.adapter = standardAdapter
        fr_recycler.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)

        vm.suggestionData.observe(this, Observer {
            if (it?.isEmpty() == true) {
                showEmptySearchCanvas()
            } else {
                showSearchCanvas()

                Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { _ ->
                    Observable.just(it)
                            .flatMap { it: List<Suggestion> ->
                                val distinctBy: List<Suggestion> = it.distinctBy { it.suggestionQuery }
                                Observable.just(distinctBy)
                            }.subscribe { it: List<Suggestion> -> standardAdapter.update(it) }
                }
            }
        })

        clear_btn.setOnClickListener {
            alert("simple", "Are you sure to delete?", okAction = {
                vm.deleteAllSuggestions()
                standardAdapter.clear()
            })
        }
    }

    private fun showEmptySearchCanvas() {
        nothing_search_id.visibility = View.VISIBLE
        search_layout_id.visibility = View.GONE
    }

    private fun showSearchCanvas() {
        nothing_search_id.visibility = View.GONE
        search_layout_id.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.push_in_left_to_right, R.anim.push_out_left_to_right)
    }

    companion object {
        const val SEARCH_QUERY = "search_query"
        const val SEARCH_QUERY_CODE = 105
    }
}
