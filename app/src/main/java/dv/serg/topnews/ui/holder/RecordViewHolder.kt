package dv.serg.topnews.ui.holder

import android.view.View
import android.widget.Button
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.app.Constants
import dv.serg.topnews.exts.context
import dv.serg.topnews.exts.getStringDateTime
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.viewmodel.RecordViewModel

class RecordViewHolder(view: View)
    : RecordViewModel.RecordViewHolder(view) {

    var adapter: StandardAdapter<Article, RecordViewModel.RecordViewHolder>? = null

    private val visitDate: TextView = view.findViewById(R.id.visit_date)
    private val historyTitle: TextView = view.findViewById(R.id.history_title)
    private val historyDescription: TextView = view.findViewById(R.id.history_desc)
    private val actionButton: Button = view.findViewById(R.id.action_button)
    private val deleteButton: Button = view.findViewById(R.id.delete_button)

    var actionButtonListener: (adapterPosition: Int) -> Unit = {}
    var deleteActionListener: (adapterPosition: Int) -> Unit = {}

    override fun onBind(position: Int, item: Article) {
        visitDate.text = getStringDateTime(context, item.publishedAt
                ?: "", Constants.Time.DEFAULT_DATETIME_PATTERN)
        historyTitle.text = item.title
        historyDescription.text = item.description
        actionButton.setOnClickListener {
            actionButtonListener.invoke(adapterPosition)
        }

        deleteButton.setOnClickListener {
            deleteActionListener.invoke(adapterPosition)
        }
    }
}

