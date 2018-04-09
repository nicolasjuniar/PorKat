package juniar.porkat.homepelanggan.transaction.menu

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.changeDateFormat
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.homepelanggan.transaction.GetTransactionModel
import juniar.porkat.homepelanggan.transaction.MenuTransactionModel
import kotlinx.android.synthetic.main.fragment_menu_transcation.*
import kotlinx.android.synthetic.main.viewholder_menu_transaction.view.*

class MenuTransactionFragment : BaseFragment<MenuTransactionPresenter>(), MenuTransactionView {

    var listMenuTransaction = mutableListOf<MenuTransactionModel>()

    companion object {
        const val ID_PESAN="id_pesan"
    }

    private val menuTransactionAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_menu_transaction, listMenuTransaction,
                { _, _, _ ->

                },
                { menuTransaction, view ->
                    with(menuTransaction) {
                        view.tv_menu.text = menuTransaction.namaMenu
                        view.tv_time.text = changeDateFormat(menuTransaction.waktuPengantaran, "yyyy-MM-dd HH:mm:ss", "HH:mm")
                        Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/menu/${this.foto}").centerCrop().resize(200,200).into(view.iv_menu)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_menu_transcation, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idPesan = arguments.getInt(ID_PESAN)
        presenter = MenuTransactionPresenter(this)
        presenter?.getListMenuTransaction(idPesan)

        with(rv_menu) {
            adapter = menuTransactionAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        swipe_layout.setOnRefreshListener {
            presenter?.getListMenuTransaction(idPesan)
        }
    }

    override fun onGetListMenuTransaction(error: Boolean, listMenu: MutableList<MenuTransactionModel>?, t: Throwable?) {
        swipe_layout.isRefreshing=false
        progressbar.hide()
        if(!error){
            listMenuTransaction.clear()
            listMenu?.let {
                listMenuTransaction.addAll(listMenu)
            }
            menuTransactionAdapter.notifyDataSetChanged()
        }else{
            t?.let {
                activity?.showShortToast(it.localizedMessage)
            }
        }
    }
}