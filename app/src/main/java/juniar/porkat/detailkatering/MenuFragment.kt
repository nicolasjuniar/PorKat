package juniar.porkat.detailkatering

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
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.show
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_menu.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

/**
 * Created by Nicolas Juniar on 26/02/2018.
 */
class MenuFragment : BaseFragment<MenuPresenter>(), MenuView {

    lateinit var listMenu: MutableList<MenuKateringModel>

    companion object {
        val ID_KATERING="id_katering"
    }

    private val menuAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_menu_katering, listMenu,
                { menu, _, _ ->
                },
                { menu, view ->
                    with(menu) {
                        view.findViewById<TextView>(R.id.tv_menu).text = this.namaMenu
                        view.findViewById<TextView>(R.id.tv_price).text = convertToIDR(this.harga.toString())
                        Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/katering/${this.foto}").centerCrop().resize(200,200).into(view.findViewById<ImageView>(R.id.iv_menu))
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_menu, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter= MenuPresenter(this)
        listMenu= mutableListOf()
        presenter?.getListMenuKatering(arguments.getInt(ID_KATERING))

        swipe_layout.setOnRefreshListener({
            presenter?.getListMenuKatering(arguments.getInt(ID_KATERING))
        })

        rv_menu.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                swipe_layout.isEnabled = topRowVerticalPosition >= 0
            }
        })
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
            with(rv_menu) {
                listMenu=list!!
                adapter = menuAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        } else {
            context.showShortToast(t?.localizedMessage!!)
        }
    }

    fun convertToIDR(harga: String): String {
        val indonesia = Locale("id", "ID")
        val indoFormat = NumberFormat.getCurrencyInstance(indonesia)
        return indoFormat.format(BigDecimal(harga))
    }
}