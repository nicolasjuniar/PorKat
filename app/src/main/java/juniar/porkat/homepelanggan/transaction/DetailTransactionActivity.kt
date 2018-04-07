package juniar.porkat.homepelanggan.transaction

import android.os.Bundle
import android.support.v7.widget.Toolbar
import juniar.porkat.R
import juniar.porkat.Utils.TabPagerAdapter
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.DETAIL_TRANSAKSI
import juniar.porkat.homepelanggan.transaction.detail.DetailTransactionFragment
import juniar.porkat.homepelanggan.transaction.invoice.InvoiceFragment
import juniar.porkat.homepelanggan.transaction.menu.MenuTransactionFragment
import kotlinx.android.synthetic.main.activity_detail_transaction.*

/**
 * Created by Jarvis on 25/03/2018.
 */
class DetailTransactionActivity : BaseActivity<Any>() {

    lateinit var transactionModel: GetTransactionModel
    private var tabAdapter = TabPagerAdapter(supportFragmentManager)

    override fun onSetupLayout() {
        setContentView(R.layout.activity_detail_transaction)
        transactionModel = intent.extras.get(DETAIL_TRANSAKSI) as GetTransactionModel
        setupToolbarTitle(toolbar_layout as Toolbar, transactionModel.namaKatering, R.drawable.ic_back_24dp)
    }

    override fun onViewReady() {

        val detailTransactionFragment = DetailTransactionFragment()
        val menuTransactionFragment = MenuTransactionFragment()
        val invoiceFragment=InvoiceFragment()
        val bundle = Bundle()
        bundle.putParcelable(DETAIL_TRANSAKSI, transactionModel)
        detailTransactionFragment.arguments = bundle
        menuTransactionFragment.arguments = bundle
        invoiceFragment.arguments=bundle
        tabAdapter.addFragment(detailTransactionFragment, getString(R.string.detail_transaction_text))
        tabAdapter.addFragment(menuTransactionFragment, getString(R.string.menu_text))
        tabAdapter.addFragment(invoiceFragment, getString(R.string.invoice_text))
        tabpager.adapter = tabAdapter
        tab_layout.setupWithViewPager(tabpager)
    }

}