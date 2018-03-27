package dv.serg.topnews.ui.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.model.Suggestion


class SearchHolder(private val view: View) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Suggestion, SearchHolder> {

    private val suggestion: TextView = view.findViewById(R.id.search_title_suggestion)

    var onClickListener: (Int, Suggestion) -> Unit = { _, _ -> }

    override fun onBind(position: Int, item: Suggestion) {
        bindSearchResultRow(item)

        view.setOnClickListener {
            onClickListener.invoke(position, item)
        }
    }

    private fun bindSearchResultRow(item: Suggestion) {
        suggestion.text = item.suggestionQuery
    }
}