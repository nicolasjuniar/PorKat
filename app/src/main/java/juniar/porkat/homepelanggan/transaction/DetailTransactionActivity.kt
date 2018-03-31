package juniar.porkat.homepelanggan.transaction

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import juniar.porkat.R
import juniar.porkat.Utils.changeDateFormat
import juniar.porkat.Utils.convertToIDR
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.DETAIL_TRANSAKSI
import juniar.porkat.common.GeneralRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_detail_transaction.*
import kotlinx.android.synthetic.main.viewholder_menu_transaction.view.*

/**
 * Created by Jarvis on 25/03/2018.
 */
class DetailTransactionActivity : BaseActivity<DetailTransactionPresenter>(), DetailTransactionView {

    lateinit var transactionModel: GetTransactionModel
    var listMenuTransaction= mutableListOf<MenuTransactionModel>()

    private val menuTransactionAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_menu_transaction, listMenuTransaction,
                { _, _, _ ->

                },
                { menuTransaction, view ->
                    with(menuTransaction) {
                        view.tv_menu.text = menuTransaction.namaMenu
                        view.tv_time.text = changeDateFormat(menuTransaction.waktuPengantaran, "yyyy-MM-dd HH:mm:ss", "HH:mm")
                    }
                })
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_detail_transaction)
        transactionModel = intent.extras.get(DETAIL_TRANSAKSI) as GetTransactionModel
        setupToolbarTitle(toolbar_layout as Toolbar, transactionModel.namaKatering, R.drawable.ic_back_24dp)
    }

    override fun onViewReady() {
        presenter= DetailTransactionPresenter(this)
        tv_status.text = transactionModel.status
        tv_total.text = transactionModel.total.toString().convertToIDR()
        tv_date.text = "${changeDateFormat(transactionModel.tglMulai, "yyyy-MM-dd", "d MMM yyyy")} - ${changeDateFormat(transactionModel.tglSelsai, "yyyy-MM-dd", "d MMM yyyy")}"
        tv_note.text = transactionModel.catatan
        presenter?.getListMenuTransaction(transactionModel.idPesan)

        with(rv_menu) {
            adapter = menuTransactionAdapter
            layoutManager = LinearLayoutManager(this@DetailTransactionActivity)
        }
    }

    override fun onGetListMenuTransaction(error: Boolean, listMenu: MutableList<MenuTransactionModel>?, t: Throwable?) {
        if (!error) {
            listMenu?.let {
                listMenuTransaction.addAll(it)
                menuTransactionAdapter.notifyDataSetChanged()
            }
        } else {
            t?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }

}