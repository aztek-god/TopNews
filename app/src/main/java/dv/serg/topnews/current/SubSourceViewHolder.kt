package dv.serg.topnews.current

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.R

class SubSourceViewHolder(private val view: View, private val clickListener: () -> Unit) : RecyclerView.ViewHolder(view),
        StandardAdapter.BindViewHolder<SubSource, SubSourceViewHolder> {

    private val name: TextView = view.findViewById(R.id.name)
    private val description: TextView = view.findViewById(R.id.description)
    private val isSelected: CheckBox = view.findViewById(R.id.is_selected)
    private val thumbnail: ImageView = view.findViewById(R.id.thumbnail)


    override fun onBind(position: Int, item: SubSource) {

        view.setOnClickListener {
            item.isSelected = !item.isSelected
            isSelected.isChecked = if (item.isSelected) true else false

            clickListener.invoke()
        }

        name.text = item.name
        description.text = item.description

        isSelected.isChecked = item.isSelected

//        ContextCompat.getDra

        thumbnail.setImageResource(R.drawable.lenta)
    }
}