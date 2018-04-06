package juniar.porkat.homekatering.menu

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.KateringModel
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.menu.MenuKateringModel
import juniar.porkat.detailkatering.menu.MenuPresenter
import juniar.porkat.detailkatering.review.MenuView
import juniar.porkat.homekatering.menu.EditMenuActivity.Companion.ACTION
import juniar.porkat.homekatering.menu.EditMenuActivity.Companion.ADD
import juniar.porkat.homekatering.menu.EditMenuActivity.Companion.DETAIL_MENU
import juniar.porkat.homekatering.menu.EditMenuActivity.Companion.EDIT
import kotlinx.android.synthetic.main.fragment_menu_katering.*
import kotlinx.android.synthetic.main.viewholder_menu_katering.view.*

class MenuKateringFragment : BaseFragment<MenuPresenter>(), MenuView {

    private var listMenu = mutableListOf<MenuKateringModel>()
    lateinit var katering: KateringModel
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    companion object {
        const val MENU_KATERING_CODE = 1
    }

    private val menuAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_menu_katering, listMenu,
                { menu, _, _ ->
                    val intent = Intent(activity, EditMenuActivity::class.java)
                    intent.putExtra(DETAIL_MENU, menu)
                    intent.putExtra(ACTION, EDIT)
                    startActivityForResult(intent, MENU_KATERING_CODE)
                },
                { menu, view ->
                    with(menu) {
                        view.tv_menu.text = this.namaMenu
                        view.tv_price.text = this.harga.toString().convertToIDR()
                        Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/menu/${this.foto}")
                                .centerCrop()
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .resize(200, 200)
                                .into(view.iv_menu)
                    }
                })
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_menu_katering, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = MenuPresenter(this)
        sharedPreferenceUtil = SharedPreferenceUtil(activity)
        katering = getProfileKatering(sharedPreferenceUtil)
        presenter?.getListMenuKatering(katering.idKatering)

        with(rv_menu) {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        fab_add.setOnClickListener {
            val intent = Intent(activity, EditMenuActivity::class.java)
            intent.putExtra(ACTION, ADD)
            startActivityForResult(intent, MENU_KATERING_CODE)
        }

        swipe_layout.setOnRefreshListener {
            presenter?.getListMenuKatering(katering.idKatering)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MENU_KATERING_CODE && resultCode == RESULT_OK) {
            presenter?.getListMenuKatering(katering.idKatering)
        }
    }

}
