package juniar.porkat.homepelanggan.transaction

import android.support.v7.widget.Toolbar
import juniar.porkat.R
import juniar.porkat.Utils.changeDateFormat
import juniar.porkat.Utils.convertToIDR
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.DETAIL_TRANSAKSI
import kotlinx.android.synthetic.main.activity_detail_transaction.*

/**
 * Created by Jarvis on 25/03/2018.
 */
class DetailTransactionActivity : BaseActivity<Any>() {

    lateinit var transactionModel: GetTransactionModel

    override fun onSetupLayout() {
        setContentView(R.layout.activity_detail_transaction)
        transactionModel = intent.extras.get(DETAIL_TRANSAKSI) as GetTransactionModel
        setupToolbarTitle(toolbar_layout as Toolbar, transactionModel.namaKatering, R.drawable.ic_back_24dp)
    }

    override fun onViewReady() {
        tv_status.text = transactionModel.status
        tv_total.text = transactionModel.total.toString().convertToIDR()
        tv_date.text = "${changeDateFormat(transactionModel.tglMulai, "yyyy-MM-dd", "d MMM yyyy")} - ${changeDateFormat(transactionModel.tglSelsai, "yyyy-MM-dd", "d MMM yyyy")}"
        tv_note.text = transactionModel.nota
    }

}