package juniar.porkat.Utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter


/**
 * Created by Nicolas Juniar on 28/12/2017.
 */
class SliderPagerAdapter(fragmentManager: FragmentManager, private val fragmentList: List<Fragment>):FragmentStatePagerAdapter(fragmentManager)
{
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}