package dv.serg.topnews.ui.view

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.topnews.R

class BottomMenuSheetDialog : BottomSheetDialogFragment() {

    private var v1: View? = null
    private var v2: View? = null
    private var v3: View? = null
    private var v4: View? = null
    private var v5: View? = null

    private var action1: () -> Unit = {}
    private var action2: () -> Unit = {}
    private var action3: () -> Unit = {}
    private var action4: () -> Unit = {}
    private var action5: () -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        v1 = view.findViewById(R.id.item_1)
        v1?.setOnClickListener { action1.invoke() }
        v2 = view.findViewById(R.id.item_2)
        v2?.setOnClickListener { action2.invoke() }
        v3 = view.findViewById(R.id.item_3)
        v3?.setOnClickListener { action3.invoke() }
        v4 = view.findViewById(R.id.item_4)
        v4?.setOnClickListener { action4.invoke() }
        v5 = view.findViewById(R.id.item_5)
        v5?.setOnClickListener { action5.invoke() }
    }

    fun setOnItem1ClickListener(action: () -> Unit) {
        action1 = action
    }

    fun setOnItem2ClickListener(action: () -> Unit) {
        action2 = action
    }

    fun setOnItem3ClickListener(action: () -> Unit) {
        action3 = action
    }

    fun setOnItem4ClickListener(action: () -> Unit) {
        action4 = action
    }

    fun setOnItem5ClickListener(action: () -> Unit) {
        action5 = action
    }
}