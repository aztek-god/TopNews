package dv.serg.topnews.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import dv.serg.topnews.ui.view.PrettySearchView
import kotlinx.android.synthetic.main.activity_search.*
import kotlin.properties.Delegates

class SearchActivity : LoggingActivity() {

    private lateinit var closeBtn: ImageButton
    private var searchQuery: String by Delegates.observable("") { _, _, newValue ->
        logd("newValue = $newValue")
        if (newValue.isEmpty()) {
            closeBtn.visibility = View.GONE
        } else {
            closeBtn.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        closeBtn = search_view.findViewById<ImageButton>(R.id.close_action).apply {
            visibility = View.GONE
        }

        search_view.setOnSearchListener(object : PrettySearchView.OnSearchListener {
            override fun onQueryChange(query: String) {
                logd("onQueryChange = $query")
                searchQuery = query
            }

            override fun onSearchAction(currentQuery: String) {
                searchQuery = currentQuery
                val resultIntent: Intent = Intent().apply {
                    putExtra(SEARCH_QUERY, searchQuery)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                overridePendingTransition(R.anim.push_in_right_to_left, R.anim.push_out_right_to_left)
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
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.push_in_left_to_right, R.anim.push_out_left_to_right)
    }

    companion object {
        const val SEARCH_QUERY = "search_query"
        const val SEARCH_QUERY_CODE = 105
    }
}
