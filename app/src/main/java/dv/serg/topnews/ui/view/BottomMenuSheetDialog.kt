package dv.serg.topnews.ui.view

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dv.serg.topnews.R
import dv.serg.topnews.app.AppContext
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

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
        v1?.setOnClickListener {
            action1.invoke()
            dismiss()
        }
        v2 = view.findViewById(R.id.item_2)
        v2?.setOnClickListener {
            action2.invoke()
            delayToast(getString(R.string.menu_delay_toast_url_copied))
            dismiss()
        }
        v3 = view.findViewById(R.id.item_3)
        v3?.setOnClickListener {
            action3.invoke()
            delayToast(getString(R.string.menu_delay_toast_image_url_copied))
            dismiss()
        }
        v4 = view.findViewById(R.id.item_4)
        v4?.setOnClickListener {
            action4.invoke()
            delayToast(getString(R.string.menu_delay_toast_temp_toast))
            dismiss()
        }
        v5 = view.findViewById(R.id.item_5)
        v5?.setOnClickListener {
            action5.invoke()
            delayToast(getString(R.string.menu_delay_toast_add_to_bookmarks))
            dismiss()
        }
    }

    private fun delayToast(msg: String) {

        Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { Toast.makeText(AppContext.appContext, msg, Toast.LENGTH_SHORT).show() }
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