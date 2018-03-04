package dv.serg.topnews.ui.view

import android.content.Context
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import dv.serg.topnews.R

class PrettySearchView(paramContext: Context, private val attrs: AttributeSet) : CardView(paramContext, attrs) {
    private val hostView: View = View.inflate(context, R.layout.search_view, this)
    private val searchAction: ImageButton = hostView.findViewById(R.id.search_action)
    private val editSearch: EditText = hostView.findViewById(R.id.edit_search)
    private val closeAction: ImageButton = hostView.findViewById(R.id.close_action)

    private var searchListener: OnSearchListener? = null

    init {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.SearchView, 0, 0)

        val leftButton = ta.getInteger(R.styleable.PrettySearchView_left_button, R.drawable.ic_arrow_back)
        val rightButton = ta.getInteger(R.styleable.PrettySearchView_right_button, R.drawable.ic_close)

        searchAction.setImageDrawable(AppCompatResources.getDrawable(context, leftButton))
        closeAction.setImageDrawable(AppCompatResources.getDrawable(context, rightButton))

        ta.recycle()

        editSearch.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                searchListener?.onSearchAction(editSearch.text.toString())
                true
            } else {
                false
            }
        }

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchListener?.onQueryChange(s.toString())
            }

        })

        searchAction.setOnClickListener {
            searchListener?.onLeftAction(it, this)
        }

        closeAction.setOnClickListener {
            searchListener?.onRightAction(it, this)
        }
    }

    fun clear() {
        editSearch.setText("".toCharArray(), 0, 0)
    }

    fun setOnSearchListener(listener: OnSearchListener) {
        this.searchListener = listener
    }

    interface OnSearchListener {
        fun onSearchAction(currentQuery: String)

        fun onLeftAction(view: View, searchView: PrettySearchView)

        fun onRightAction(view: View, searchView: PrettySearchView)

        fun onQueryChange(query: String)
    }
}