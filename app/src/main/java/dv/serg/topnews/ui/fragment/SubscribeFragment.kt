package dv.serg.topnews.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.lib.utils.logd
import dv.serg.topnews.R

class SubscribeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_foo, parent, false);
        logd("SubscribeFragment:onCreateView")
        return inflater.inflate(R.layout.fr_subscribe_layout, container, true)
    }
}