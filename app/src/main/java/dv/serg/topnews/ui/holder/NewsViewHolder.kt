package dv.serg.topnews.ui.holder

import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.lib.utils.loge
import dv.serg.topnews.R
import dv.serg.topnews.app.AppContext
import dv.serg.topnews.app.Constants
import dv.serg.topnews.exts.*
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.view.BottomMenuSheetDialog


class NewsViewHolder(view: View)
    : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Article, NewsViewHolder> {

    private val root: View = view.findViewById(R.id.news_item_root_id)
    private val source: TextView = view.findViewById(R.id.source_name)
    private val content: TextView = view.findViewById(R.id.content)
    private val datetime: TextView = view.findViewById(R.id.datetime)
    private val header: TextView = view.findViewById(R.id.header)
    private val thumb: ImageView = view.findViewById(R.id.thumb)
    private val bottomMenu = BottomMenuSheetDialog()

    var addToFilterAction: (item: Article) -> Unit = {}
    var addToBookmarkAction: (item: Article) -> Unit = {}
    var shortClickListener: (item: Article) -> Unit = {}
    var copyTextAction: (url: String?) -> Unit = { url ->
        context.copyToClipboard(url ?: throw Exception("Item's url is $url"), "Copied")
    }

    var fm: FragmentManager? = null

    override fun onBind(position: Int, item: Article) {
        try {
            source.text = item.source?.name
            content.text = item.description
            datetime.text = getStringDateTime(context, item.publishedAt
                    ?: "", Constants.Time.DEFAULT_DATETIME_PATTERN)
            header.text = item.title

            thumb.load(item.urlToImage)

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
                apply { setOnClickListener { shortClickListener.invoke(item) } }
            }
        } catch (ex: Exception) {
            loge(ex.printStackTrace().toString())
        }
    }
}