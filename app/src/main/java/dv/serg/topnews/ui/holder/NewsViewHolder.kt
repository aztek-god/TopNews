package dv.serg.topnews.ui.holder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.app.AppContext
import dv.serg.topnews.app.Constants
import dv.serg.topnews.app.load
import dv.serg.topnews.app.openBrowser
import dv.serg.topnews.model.Article

class NewsViewHolder(private val view: View, private val throwableHandler: (Throwable) -> Unit)
    : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Article, NewsViewHolder> {

    private val context: Context get() = view.context

    private val source: TextView = view.findViewById(R.id.source)
    private val content: TextView = view.findViewById(R.id.content)
    private val datetime: TextView = view.findViewById(R.id.datetime)
    private val header: TextView = view.findViewById(R.id.header)

    private val thumb: ImageView = view.findViewById(R.id.thumb)

    private val button: Button = view.findViewById(R.id.button123)

    override fun onBind(position: Int, item: Article) {
        try {
            source.text = item.source?.name
            content.text = item.description
            datetime.text = item.publishedAt
            header.text = item.title
            // todo place error messages to strings.xml for internationalization purpose
            thumb.load(item.urlToImage
                    ?: throw LoadImageException("Unable to load image as placeholder with invalid url. The url's value is ${item.urlToImage}"))

//            Glide.with(context).load(item.url)

            button.setOnClickListener {
                context.openBrowser(item.url
                        ?: throw OpenBrowserException("Unable to open browser with invalid url. The url's value is ${item.url}"),
                        AppContext.getStringByName(Constants.Resources.CHOOSER_TITLE))
            }
        } catch (ex: Exception) {
            throwableHandler.invoke(ex)
        }
    }

    class OpenBrowserException(message: String) : Exception(message)
    class LoadImageException(message: String) : Exception(message)
}