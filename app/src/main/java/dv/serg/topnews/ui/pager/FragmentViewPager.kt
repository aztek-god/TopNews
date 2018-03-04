package dv.serg.topnews.ui.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import dv.serg.lib.utils.logd
import dv.serg.topnews.app.AppContext
import dv.serg.topnews.app.Constants
import dv.serg.topnews.ui.fragment.InfoFragment

class FragmentViewPager(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        logd("FragmentViewPager:getItem:position = $position")

        return retrieveFragment(position)
    }

    private fun retrieveFragment(pos: Int): Fragment {
        return InfoFragment.newInstance(Constants.SourceType.FRAGMENTS[pos]
                ?: throw Exception("Is seems there is no fragment with such id."))
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return AppContext.getStringByName(Constants.SourceType.FRAGMENTS[position]
                ?: throw Exception("Is seems there is no fragment with such id."))
    }

    override fun getCount(): Int {
        return Constants.SourceType.FRAGMENTS.size
    }
}