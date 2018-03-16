package dv.serg.topnews.ui.holder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dv.serg.lib.android.context.v4.toastShort
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R
import dv.serg.topnews.app.Constants
import dv.serg.topnews.app.load
import dv.serg.topnews.model.Article
import dv.serg.topnews.ui.view.BottomMenuSheetDialog
import dv.serg.topnews.util.getStringDateTime
import dv.serg.topnews.util.openBrowser


class NewsViewHolder(private val view: View, private val throwableHandler: (Throwable) -> Unit)
    : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Article, NewsViewHolder> {

    private val context: Context get() = view.context

    private val root: View = view.findViewById(R.id.news_item_root_id)
    private val source: TextView = view.findViewById(R.id.source)
    private val content: TextView = view.findViewById(R.id.content)
    private val datetime: TextView = view.findViewById(R.id.datetime)
    private val header: TextView = view.findViewById(R.id.header)

    private val thumb: ImageView = view.findViewById(R.id.thumb)

//    private val button: Button = view.findViewById(R.id.button123)

    private val bottomMenu = BottomMenuSheetDialog()

    var addToFilterAction: (item: Article) -> Unit = {}
    var addToBookmarkAction: (item: Article) -> Unit = {}

    var shortClickListener: (item: Article) -> Unit = {}

    var fm: FragmentManager? = null

    override fun onBind(position: Int, item: Article) {
        try {
            source.text = item.source?.name
            content.text = item.description
//            getStringDateTime()
            datetime.text = getStringDateTime(context, item.publishedAt
                    ?: "", Constants.Time.DEFAULT_DATETIME_PATTERN)
            header.text = item.title

            // todo place error messages to strings.xml for internationalization purpose
            thumb.load(item.urlToImage
                    ?: throw LoadImageException("Unable to load image as placeholder with invalid url. The url's value is ${item.urlToImage}"))



            bottomMenu.apply {
                setOnItem1ClickListener {
                    openBrowser(context!!, item.url!!)
                    toastShort("!!!")
                    dismiss()
                }
                setOnItem2ClickListener {
                    val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Copied", item.url)
                    clipboard.primaryClip = clip
                    dismiss()
                }
                setOnItem3ClickListener {
                    val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Copied", item.urlToImage)
                    clipboard.primaryClip = clip
                    dismiss()
                }
                setOnItem4ClickListener {
                    addToFilterAction.invoke(item)
                    dismiss()
                }
                setOnItem5ClickListener {
                    addToBookmarkAction.invoke(item)
                    dismiss()
                }
            }

            root.setOnLongClickListener {
                bottomMenu.show(fm, bottomMenu.tag)
                true
            }

            root.setOnClickListener {
                shortClickListener.invoke(item)
            }

        } catch (ex: Exception) {
            throwableHandler.invoke(ex)
        }
    }


    class OpenBrowserException(message: String) : Exception(message)
    class LoadImageException(message: String) : Exception(message)
}