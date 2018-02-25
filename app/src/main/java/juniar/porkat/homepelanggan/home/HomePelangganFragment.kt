package juniar.porkat.homepelanggan.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.Utils.TabPagerAdapter
import juniar.porkat.common.BaseFragment
import juniar.porkat.homescreen.FragmentKateringDistance
import juniar.porkat.homescreen.FragmentKateringRating
import kotlinx.android.synthetic.main.fragment_home_pelanggan.*

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
class HomePelangganFragment : BaseFragment<Any>() {

    lateinit var tabAdapter: TabPagerAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_home_pelanggan, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        tabAdapter = TabPagerAdapter(childFragmentManager)
        super.onViewCreated(view, savedInstanceState)
        tabAdapter.addFragment(FragmentKateringRating(), "Rekomendasi")
        tabAdapter.addFragment(FragmentKateringDistance(), "Sekitar")
        tabpager.adapter = tabAdapter
        tab_layout.setupWithViewPager(tabpager)
    }
}