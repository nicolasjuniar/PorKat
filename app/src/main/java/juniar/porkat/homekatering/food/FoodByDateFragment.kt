package juniar.porkat.homekatering.food

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.getProfileKatering
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.showShortToast
import juniar.porkat.auth.KateringModel
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_menu_by_date.*
import kotlinx.android.synthetic.main.viewholder_menu_by_date.view.*

class FoodByDateFragment : BaseFragment<FoodByDatePresenter>(),FoodByDateView {

    val listMenu = mutableListOf<MenuByDateModel>()
    lateinit var katering: KateringModel
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    private val menuAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_menu_by_date, listMenu,
                { _, _, _ ->
                },
                { menu, view ->
                    with(menu) {
                        view.tv_menu.text = this.namaMenu
                        view.tv_amount.text = getString(R.string.amount_menu_text,this.jumlah)
                        Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/menu/${this.foto}")
                                .centerCrop()
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .resize(200, 200)
                                .into(view.iv_menu)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_menu_by_date, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter=FoodByDatePresenter(this)
        sharedPreferenceUtil = SharedPreferenceUtil(activity)
        katering = getProfileKatering(sharedPreferenceUtil)

        with(rv_menu){
            adapter=menuAdapter
            layoutManager=LinearLayoutManager(activity)
        }

        presenter?.getListMakananToday(katering.idKatering)

        swipe_layout.setOnRefreshListener {
            presenter?.getListMakananToday(katering.idKatering)
        }
    }

    override fun onGetListMakanan(error: Boolean, listMenu: MutableList<MenuByDateModel>?, t: Throwable?) {
        swipe_layout.isRefreshing=false
        progressbar.hide()
        if(!error){
            this.listMenu.clear()
            listMenu?.let {
                this.listMenu.addAll(it)
            }
            menuAdapter.notifyDataSetChanged()
        }else{
            t?.let {
                activity?.showShortToast(it.localizedMessage)
            }
        }
    }
}