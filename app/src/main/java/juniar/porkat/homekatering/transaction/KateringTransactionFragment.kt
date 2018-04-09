package juniar.porkat.homekatering.transaction

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant.CommonStrings.Companion.DETAIL_TRANSAKSI
import juniar.porkat.common.GeneralRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_katering_transaction.*
import kotlinx.android.synthetic.main.viewholder_history_transaction.view.*

class KateringTransactionFragment : BaseFragment<KateringTransactionPresenter>(), KateringTransactionView {

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    private var listTransaction = mutableListOf<KateringTransactionModel>()

    private val transactionAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_history_transaction, listTransaction,
                { transaction, _, _ ->
                    val intent = Intent(activity, DetailTransactionKateringActivity::class.java)
                    intent.putExtra(DETAIL_TRANSAKSI, transaction)
                    startActivity(intent)
                },
                { transaction, view ->
                    with(transaction) {
                        view.tv_katering.text = transaction.namaLengkap
                        view.tv_status.text = transaction.status
                        view.tv_date.text = "${juniar.porkat.Utils.changeDateFormat(transaction.tglMulai, "yyyy-MM-dd", "d MMM yyyy")} - ${juniar.porkat.Utils.changeDateFormat(transaction.tglSelesai, "yyyy-MM-dd", "d MMM yyyy")}"
                        view.tv_total.text = transaction.total.toString().convertToIDR()
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_katering_transaction, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter= KateringTransactionPresenter(this)
        sharedPreferenceUtil= SharedPreferenceUtil(activity)
        presenter?.getListTransactionKatering(getProfileKatering(sharedPreferenceUtil).idKatering)

        with(rv_transaction){
            adapter=transactionAdapter
            layoutManager=LinearLayoutManager(activity)
        }

        swipe_layout.setOnRefreshListener {
            presenter?.getListTransactionKatering(getProfileKatering(sharedPreferenceUtil).idKatering)
        }

    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            progressbar.show()
        } else {
            progressbar.hide()
            swipe_layout.isRefreshing=false
        }
    }

    override fun onGetListTransactionKatering(error: Boolean, listTransaction: MutableList<KateringTransactionModel>?, t: Throwable?) {
        setLoading(false)
        if(!error){
            this.listTransaction.clear()
            listTransaction?.let {
                this.listTransaction.addAll(it)
            }
            transactionAdapter.notifyDataSetChanged()
        }else{
            t?.let {
                activity?.showShortToast(it.localizedMessage)
            }
        }
    }

}