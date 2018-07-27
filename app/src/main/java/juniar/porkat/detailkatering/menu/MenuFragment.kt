package juniar.porkat.detailkatering.menu

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.convertToIDR
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.show
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.review.MenuView
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.viewholder_menu_katering.view.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

/**
 * Created by Nicolas Juniar on 26/02/2018.
 */
class MenuFragment : BaseFragment<MenuPresenter>(), MenuView {

    private var listMenu= mutableListOf<MenuKateringModel>()

    companion object {
        const val ID_KATERING="id_katering"
    }

    private val menuAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_menu_katering, listMenu,
                { _, _, _ ->

                },
                { menu, view ->
                    with(menu) {
                        view.tv_menu.text = this.namaMenu
                        view.tv_price.text = this.harga.toString().convertToIDR()
                        Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/menu/${this.foto}").centerCrop().resize(200,200).into(view.iv_menu)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_menu, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter= MenuPresenter(this)
        presenter?.getListMenuKatering(arguments.getInt(ID_KATERING))

        with(rv_menu) {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        swipe_layout.setOnRefreshListener {
            presenter?.getListMenuKatering(arguments.getInt(ID_KATERING))
        }
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            progressbar.show()
        } else {
            progressbar.hide()
        }
    }

    override fun onGetListMenu(error: Boolean, list: MutableList<MenuKateringModel>?, t: Throwable?) {
        setLoading(false)
        swipe_layout.isRefreshing = false
        if (!error) {
            listMenu.clear()
            list?.let {
                listMenu.addAll(it)
            }
            menuAdapter.notifyDataSetChanged()
        } else {
            t?.let {
                activity?.showShortToast(it.localizedMessage)
            }
        }
    }
}