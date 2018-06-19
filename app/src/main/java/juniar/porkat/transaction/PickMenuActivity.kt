package juniar.porkat.transaction

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.convertToIDR
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.show
import juniar.porkat.Utils.showShortToast
import juniar.porkat.auth.login.LoginActivity
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.menu.MenuFragment.Companion.ID_KATERING
import juniar.porkat.detailkatering.menu.MenuKateringModel
import juniar.porkat.detailkatering.menu.MenuPresenter
import juniar.porkat.detailkatering.review.MenuView
import kotlinx.android.synthetic.main.activity_pick_menu.*
import kotlinx.android.synthetic.main.viewholder_menu_katering.view.*
import java.util.*

/**
 * Created by Jarvis on 18/03/2018.
 */
class PickMenuActivity : BaseActivity<MenuPresenter>(), MenuView {

    lateinit var listMenu: MutableList<MenuKateringModel>

    companion object {
        val PICKED_MENU = "picked_menu"
    }

    private val menuAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_menu_katering, listMenu,
                { menu, _, _ ->
                    val intent = Intent()
                    intent.putExtra(PICKED_MENU, menu)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                },
                { menu, view ->
                    with(menu) {
                        view.tv_menu.text = this.namaMenu
                        view.tv_price.text = this.harga.toString().convertToIDR()
                        Picasso.with(this@PickMenuActivity).load("${PorkatApp.BASE_URL}/foto/menu/${this.foto}").centerCrop().resize(200, 200).into(view.iv_menu)
                    }
                })
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_pick_menu)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.available_menu, R.drawable.ic_back_24dp)
    }

    override fun onViewReady() {
        presenter = MenuPresenter(this)
        listMenu = mutableListOf()
        val idKatering = intent.getIntExtra(ID_KATERING, -1)
        presenter?.getListMenuKatering(idKatering)

        swipe_layout.setOnRefreshListener({
            presenter?.getListMenuKatering(idKatering)
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
                listMenu = list!!
                adapter = menuAdapter
                layoutManager = LinearLayoutManager(this@PickMenuActivity)
            }
        } else {
            showShortToast(t?.localizedMessage!!)
        }
    }

}
