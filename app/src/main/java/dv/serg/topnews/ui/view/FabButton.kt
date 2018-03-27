package dv.serg.topnews.ui.view

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.View
import dv.serg.topnews.R
import dv.serg.topnews.exts.hideAnimation
import dv.serg.topnews.exts.showAnimation

class FabButton(mContext: Context, attrs: AttributeSet? = null) : AppCompatButton(mContext, attrs) {
    init {
        background.setColorFilter(ContextCompat.getColor(mContext, R.color.drop_btn_color), PorterDuff.Mode.MULTIPLY)
    }

    val isVisible: Boolean get() = visibility == View.VISIBLE
    val isGone: Boolean get() = visibility == View.GONE
    val isInvisible: Boolean get() = visibility == View.INVISIBLE

    fun hide() {
        hideAnimation()
    }

    fun show() {
        showAnimation()
    }
}
