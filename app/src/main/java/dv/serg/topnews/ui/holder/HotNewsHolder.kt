package dv.serg.topnews.ui.holder

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.app.AppContext
import dv.serg.topnews.app.load
import dv.serg.topnews.model.Article


class HotNewsHolder(private val view: View) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Article, HotNewsHolder> {

    private val sourceName: TextView = view.findViewById(R.id.source)
    private val thumb: ImageView = view.findViewById(R.id.thumb)
    private val description: TextView = view.findViewById(R.id.description)
    private val publishedAt: TextView = view.findViewById(R.id.published_at)

    override fun onBind(position: Int, item: Article) {
        sourceName.text = item.sourceName
        // todo if there is no image, for example ref is equal to null, then use default error placeholder
        thumb.load(item.urlToImage ?: "")
        description.text = item.description
        publishedAt.text = AppContext.getStringDate(item.publishedAt ?: "")

        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            view.context.startActivity(intent)
        }
    }
}