package dv.serg.topnews.ui.fragment

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.topnews.R
import dv.serg.topnews.ui.pager.FragmentViewPager
import kotlinx.android.synthetic.main.content_top_news.*

class HotNewsFragment : LoggingFragment() {

    private var parentOwner: LifecycleOwner? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        parentOwner = context as LifecycleOwner
    }

    override fun onDetach() {
        super.onDetach()
        parentOwner = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.content_top_news, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.apply {
            adapter = FragmentViewPager(childFragmentManager)

            tabLayout.setupWithViewPager(viewPager)
        }
    }

    companion object {
        fun newInstance(): HotNewsFragment {
            return HotNewsFragment()
        }
    }
}
