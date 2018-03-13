package dv.serg.topnews.ui.holder

import android.view.View
import android.widget.Button
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.logd
import dv.serg.topnews.R
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.viewmodel.RecordViewModel

class HistoryViewHolder(view: View,
                        private val actionButtonCallback: (pos: Int) -> Unit,
                        private val actionDeleteCallback: (pos: Int) -> Unit)
    : RecordViewModel.RecordViewHolder(view) {

    var adapter: StandardAdapter<Article, RecordViewModel.RecordViewHolder>? = null

    private val visitDate: TextView = view.findViewById(R.id.visit_date)
    private val historyTitle: TextView = view.findViewById(R.id.history_title)
    private val historyDescription: TextView = view.findViewById(R.id.history_desc)
    private val actionButton: Button = view.findViewById(R.id.action_button)
    private val deleteButton: Button = view.findViewById(R.id.delete_button)

    override fun onBind(position: Int, item: Article) {
        visitDate.text = item.publishedAt
        historyTitle.text = item.title
        historyDescription.text = item.description
        actionButton.setOnClickListener {
            logd("Action button pressed.")
            // todo implement view page logic
            actionButtonCallback.invoke(adapterPosition)
        }

        deleteButton.setOnClickListener {
            adapter!!.removeAt(adapterPosition)
            actionDeleteCallback.invoke(adapterPosition)
        }
    }
}