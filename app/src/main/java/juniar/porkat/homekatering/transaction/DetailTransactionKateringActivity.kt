package juniar.porkat.homekatering.transaction

import android.os.Bundle
import android.support.v7.widget.Toolbar
import juniar.porkat.R
import juniar.porkat.Utils.TabPagerAdapter
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant
import juniar.porkat.homekatering.transaction.detail.DetailTransactionKateringFragment
import juniar.porkat.homepelanggan.transaction.detail.DetailTransactionPelangganFragment
import juniar.porkat.homepelanggan.transaction.menu.MenuTransactionFragment
import kotlinx.android.synthetic.main.activity_detail_transaction_pelanggan.*

class DetailTransactionKateringActivity : BaseActivity<Any>() {

    lateinit var transactionModel: KateringTransactionModel
    private var tabAdapter = TabPagerAdapter(supportFragmentManager)

    override fun onSetupLayout() {
        setContentView(R.layout.activity_detail_transaction_katering)
        transactionModel = intent.extras.get(Constant.CommonStrings.DETAIL_TRANSAKSI) as KateringTransactionModel
        setupToolbarTitle(toolbar_layout as Toolbar, transactionModel.namaLengkap, R.drawable.ic_back_24dp)
    }

    override fun onViewReady() {
        val detailTransactionFragment=DetailTransactionKateringFragment()
        val menuTransactionFragment=MenuTransactionFragment()
        val bundle = Bundle()
        bundle.putInt(MenuTransactionFragment.ID_PESAN,transactionModel.idPesan)
        bundle.putParcelable(Constant.CommonStrings.DETAIL_TRANSAKSI, transactionModel)
        detailTransactionFragment.arguments = bundle
        menuTransactionFragment.arguments = bundle
        tabAdapter.addFragment(detailTransactionFragment, getString(R.string.detail_transaction_text))
        tabAdapter.addFragment(menuTransactionFragment, getString(R.string.menu_text))
        tabpager.adapter = tabAdapter
        tab_layout.setupWithViewPager(tabpager)
    }

}