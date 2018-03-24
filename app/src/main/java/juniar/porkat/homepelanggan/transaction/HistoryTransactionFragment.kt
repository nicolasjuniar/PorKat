package juniar.porkat.homepelanggan.transaction

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_history_transaction.*
import kotlinx.android.synthetic.main.viewholder_history_transaction.view.*

/**
 * Created by Jarvis on 21/03/2018.
 */
class HistoryTransactionFragment : BaseFragment<HistoryTransactionPresenter>(), HistoryTransactionView {

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    var listHistoryTransaction = mutableListOf<GetTransactionModel>()

    private val transactionAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_history_transaction, listHistoryTransaction,
                { transaction, _, _ ->

                },
                { transaction, view ->
                    with(transaction) {
                        view.tv_katering.text = transaction.namaKatering
                        view.tv_status.text = transaction.status
                        view.tv_date.text = "${changeDateFormat(transaction.tglMulai,"yyyy-MM-dd","d MMM yyyy")} - ${changeDateFormat(transaction.tglSelsai,"yyyy-MM-dd","d MMM yyyy")}"
                        view.tv_total.text = transaction.total.toString().convertToIDR()
                    }
                })
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_history_transaction, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = HistoryTransactionPresenter(this)
        sharedPreferenceUtil = SharedPreferenceUtil(activity)
        presenter?.getListTransactionPelanggan(getProfilePelanggan(sharedPreferenceUtil).idPelanggan)

        with(rv_transaction){
            adapter=transactionAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            progressbar.show()
        } else {
            progressbar.hide()
        }
    }

    override fun onGetListTransactionPelanggan(error: Boolean, listTransaction: MutableList<GetTransactionModel>?, t: Throwable?) {
        setLoading(false)
        if(!error){
            listHistoryTransaction.clear()
            listTransaction?.let {
                listHistoryTransaction.addAll(it)
            }
            transactionAdapter.notifyDataSetChanged()
        }else{
            t?.let {
                activity?.showShortToast(it.localizedMessage)
            }
        }
    }

}