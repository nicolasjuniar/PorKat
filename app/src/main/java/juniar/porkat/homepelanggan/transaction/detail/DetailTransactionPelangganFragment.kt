package juniar.porkat.homepelanggan.transaction.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.Utils.changeDateFormat
import juniar.porkat.Utils.convertToIDR
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant.CommonStrings.Companion.DETAIL_TRANSAKSI
import juniar.porkat.homepelanggan.transaction.GetTransactionModel
import kotlinx.android.synthetic.main.fragment_detail_transaction.*

class DetailTransactionPelangganFragment : BaseFragment<Any>() {

    lateinit var transactionModel: GetTransactionModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_detail_transaction, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionModel = arguments.getParcelable(DETAIL_TRANSAKSI) as GetTransactionModel

        tv_status.text = transactionModel.status
        tv_total.text = transactionModel.total.toString().convertToIDR()
        tv_date.text = "${changeDateFormat(transactionModel.tglMulai, "yyyy-MM-dd", "d MMM yyyy")} - ${changeDateFormat(transactionModel.tglSelesai, "yyyy-MM-dd", "d MMM yyyy")}"
        tv_note.text = transactionModel.catatan
    }
}