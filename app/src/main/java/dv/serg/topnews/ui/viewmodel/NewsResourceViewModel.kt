package dv.serg.topnews.ui.viewmodel

import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.current.SubSource
import dv.serg.topnews.current.SubSourceViewHolder
import dv.serg.topnews.current.SubSourceViewModel

class NewsResourceViewModel(private val repo: SubSourceViewModel.Contract.Repository) {

    var adapter: StandardAdapter<SubSource, SubSourceViewHolder>? = null


}