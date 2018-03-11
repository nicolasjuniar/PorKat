package juniar.porkat.detailkatering

import android.os.Bundle
import android.support.v7.widget.Toolbar
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.TabPagerAdapter
import juniar.porkat.Utils.encodeJson
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.SESSION
import juniar.porkat.detailkatering.deskripsi.DeskripsiKateringFragment.Companion.DESKRIPSI
import juniar.porkat.detailkatering.menu.MenuFragment.Companion.ID_KATERING
import juniar.porkat.detailkatering.deskripsi.DeskripsiKateringFragment
import juniar.porkat.detailkatering.menu.MenuFragment
import juniar.porkat.detailkatering.review.ReviewFragment
import juniar.porkat.homescreen.GetKateringModel
import kotlinx.android.synthetic.main.activity_detail_katering.*

/**
 * Created by Nicolas Juniar on 24/02/2018.
 */
class DetailKateringActivity : BaseActivity<Any>() {
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    var tabAdapter = TabPagerAdapter(supportFragmentManager)

    companion object {
        val DETAIL_KATERING = "detail_katering"
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_detail_katering)
        setupToolbarTitle(toolbar_layout as Toolbar)
    }

    override fun onViewReady() {
        sharedPreferenceUtil= SharedPreferenceUtil(this@DetailKateringActivity)
        if(sharedPreferenceUtil.getBoolean(SESSION)){
            fab_transaction.show()
        }
        val katering = intent.extras.get(DETAIL_KATERING) as GetKateringModel
        changeTitleToolbar(katering.nama_katering)
        val bundle = Bundle()
        bundle.putString(DESKRIPSI, katering.encodeJson())
        bundle.putInt(ID_KATERING, katering.id_katering)
        val deskripsiKateringFragment = DeskripsiKateringFragment()
        deskripsiKateringFragment.arguments = bundle
        val menuFragment = MenuFragment()
        menuFragment.arguments = bundle
        val reviewFragment = ReviewFragment()
        reviewFragment.arguments = bundle
        tabAdapter.addFragment(deskripsiKateringFragment, getString(R.string.deskripsi_text))
        tabAdapter.addFragment(menuFragment, getString(R.string.menu_text))
        tabAdapter.addFragment(reviewFragment, getString(R.string.ulasan_text))
        tabpager.adapter = tabAdapter
        tab_layout.setupWithViewPager(tabpager)

        fab_transaction.setOnClickListener {
            showShortToast("tenot")
        }
    }

}