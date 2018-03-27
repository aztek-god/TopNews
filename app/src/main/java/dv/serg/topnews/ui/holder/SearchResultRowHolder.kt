package dv.serg.topnews.ui.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.model.Suggestion


class SearchResultRowHolder(view: View) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Suggestion, SearchPrimaryRowHolder> {
    val suggestion: TextView = view.findViewById(R.id.search_title_suggestion)

    override fun onBind(position: Int, item: Suggestion) {
        suggestion.text = item.suggestionQuery
    }
}