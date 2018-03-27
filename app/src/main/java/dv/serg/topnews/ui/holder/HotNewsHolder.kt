package dv.serg.topnews.ui.holder

import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.app.AppContext
import dv.serg.topnews.app.Constants
import dv.serg.topnews.exts.*
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.view.BottomMenuSheetDialog


class HotNewsHolder(view: View) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Article, HotNewsHolder> {

    private val sourceName: TextView = view.findViewById(R.id.source_name)
    private val thumb: ImageView = view.findViewById(R.id.thumb)
    private val description: TextView = view.findViewById(R.id.description)
    private val publishedAt: TextView = view.findViewById(R.id.published_at)
    private val bottomMenu = BottomMenuSheetDialog()

    private val root: View = view.findViewById(R.id.hot_news_id)

    var fm: FragmentManager? = null

    var addToFilterAction: (item: Article) -> Unit = {}
    var addToBookmarkAction: (item: Article) -> Unit = {}
    var clickListener: (item: Article) -> Unit = {}
    var copyTextAction: (url: String?) -> Unit = { url ->
        context.copyToClipboard(url ?: throw Exception("Item's url is $url"), "Copied")
    }

    override fun onBind(position: Int, item: Article) {

        sourceName.text = item.sourceName

        thumb.load(item.urlToImage ?: "")
        description.text = item.description
        publishedAt.text = getStringDateTime(context, item.publishedAt
                ?: "", Constants.Time.DEFAULT_DATETIME_PATTERN)

        with(bottomMenu) {
            apply {
                setOnItem1ClickListener {
                    openBrowser(context ?: AppContext.appContext, item.url ?: "")
                }
            }
            apply { setOnItem2ClickListener { copyTextAction(item.urlToImage) } }
            apply { setOnItem3ClickListener { copyTextAction(item.url) } }
            apply { setOnItem4ClickListener { addToFilterAction.invoke(item) } }
            apply { setOnItem5ClickListener { addToBookmarkAction.invoke(item) } }
        }

        with(root) {
            apply {
                setOnLongClickListener {
                    bottomMenu.show(fm, bottomMenu.tag)
                    true
                }
            }
        }

        root.setOnClickListener { clickListener.invoke(item) }
    }
}