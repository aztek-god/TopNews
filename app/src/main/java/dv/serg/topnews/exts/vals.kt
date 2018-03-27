package dv.serg.topnews.exts

import android.content.Context
import android.support.v7.widget.RecyclerView

val RecyclerView.ViewHolder.context: Context get() = itemView.context
