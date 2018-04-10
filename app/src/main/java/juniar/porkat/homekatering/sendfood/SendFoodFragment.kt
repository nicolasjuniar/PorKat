package juniar.porkat.homekatering.sendfood

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.KateringModel
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_send_food.*
import kotlinx.android.synthetic.main.viewholder_send_food.view.*

class SendFoodFragment : BaseFragment<SendFoodPresenter>(), SendFoodView {

    val listSendFood = mutableListOf<SendFoodModel>()
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    lateinit var kateringModel: KateringModel


    private val sendFoodAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_send_food, listSendFood,
                { _, _, _ ->
                },
                { sendFood, view ->
                    with(sendFood) {
                        view.tv_name.text = sendFood.namaMenu
                        view.tv_address.text = sendFood.alamat
                        view.tv_time.text = changeDateFormat(sendFood.waktuPengantaran, "yyyy-MM-dd HH:mm:ss", "HH:mm")
                        val difference= getDifferenceTime(changeDateFormat(sendFood.waktuPengantaran, "yyyy-MM-dd HH:mm:ss", "HH:mm"))
                        when
                        {
                            difference>=3 -> {
                                view.tv_time.text=getString(R.string.difference_time_text,difference.toString())
                                view.ic_indicator.setImageResource(R.color.Green)
                            }
                            difference>=1 -> {
                                view.tv_time.text=getString(R.string.difference_time_text,difference.toString())
                                view.ic_indicator.setImageResource(R.color.Yellow)
                            }
                            difference>0 ->{
                                view.tv_time.text=getString(R.string.less_than_one_hour)
                                view.ic_indicator.setImageResource(R.color.Red)
                            }
                            else->{
                                view.tv_time.text=getString(R.string.late_text)
                                view.ic_indicator.setImageResource(R.color.Red)
                            }
                        }
                        Picasso.with(activity).load("${juniar.porkat.PorkatApp.BASE_URL}/foto/menu/${this.foto}").centerCrop().resize(200, 200).into(view.iv_menu)
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_send_food, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferenceUtil=SharedPreferenceUtil(activity)
        kateringModel = getProfileKatering(sharedPreferenceUtil)
        presenter = SendFoodPresenter(this)
        presenter?.getListSendFood(kateringModel.idKatering)

        with(rv_transaction) {
            adapter = sendFoodAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        swipe_layout.setOnRefreshListener {
            presenter?.getListSendFood(kateringModel.idKatering)
        }
    }

    override fun onGetListSendFood(error: Boolean, listSendFood: MutableList<SendFoodModel>?, t: Throwable?) {
        progressbar.hide()
        swipe_layout.isRefreshing = false
        if (!error) {
            this.listSendFood.clear()
            listSendFood?.let {
                this.listSendFood.addAll(it)
            }
            sendFoodAdapter.notifyDataSetChanged()
        } else {
            t?.let {
                activity?.showShortToast(it.localizedMessage)
            }
        }
    }

}