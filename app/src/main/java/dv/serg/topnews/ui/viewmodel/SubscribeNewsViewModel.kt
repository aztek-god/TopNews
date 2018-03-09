package dv.serg.topnews.ui.viewmodel

import android.arch.lifecycle.ViewModel
import dv.serg.lib.collection.StandardAdapter
import dv.serg.topnews.current.SubSource
import dv.serg.topnews.current.SubSourceViewHolder
import dv.serg.topnews.current.SubSourceViewModel


class SubscribeNewsViewModel(private val repo: SubSourceViewModel.Contract.Repository) : ViewModel() {

    var subscribeAdapter: StandardAdapter<SubSource, SubSourceViewHolder>? = null


}