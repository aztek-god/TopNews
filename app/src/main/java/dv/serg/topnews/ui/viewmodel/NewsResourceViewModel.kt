package dv.serg.topnews.ui.viewmodel

import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.model.SubSource
import dv.serg.topnews.ui.holder.SubSourceViewHolder

class NewsResourceViewModel(private val repo: SubSourceViewModel.Contract.Repository) {

    var adapter: StandardAdapter<SubSource, SubSourceViewHolder>? = null


}