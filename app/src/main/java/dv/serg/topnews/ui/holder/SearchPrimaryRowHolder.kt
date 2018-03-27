package dv.serg.topnews.ui.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.model.Suggestion

class SearchPrimaryRowHolder(view: View) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Suggestion, SearchPrimaryRowHolder> {

    val clearBtn: Button = view.findViewById(R.id.clear_btn)

    override fun onBind(position: Int, item: Suggestion) {
    }
}