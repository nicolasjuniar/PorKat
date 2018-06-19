package juniar.porkat.homekatering.sendfood

import android.app.Activity
import android.app.Activity.RESULT_OK
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
import juniar.porkat.homekatering.sendfood.SendFoodMapActivity.Companion.SEND_FOOD
import kotlinx.android.synthetic.main.fragment_send_food.*
import kotlinx.android.synthetic.main.viewholder_send_food.view.*

class SendFoodFragment : BaseFragment<SendFoodPresenter>(), SendFoodView {

    val listSendFood = mutableListOf<SendFoodModel>()
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    lateinit var kateringModel: KateringModel
    val listSendingFood = mutableListOf<SendFoodModel>()
    var transaction = false

    companion object {
        const val LIST_SEND_FOOD = "list_send_food"
    }


    private val sendFoodAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_send_food, listSendFood,
                { sendFood, _, view ->
                    if (transaction) {
                        view.cb_transaction.isChecked = !view.cb_transaction.isChecked
                        if (view.cb_transaction.isChecked) listSendingFood.add(sendFood) else listSendingFood.remove(sendFood)
                    }
                },
                { sendFood, view ->
                    with(sendFood) {
                        if (transaction) {
                            view.cb_transaction.show()
                        } else {
                            view.cb_transaction.hide()
                        }
                        view.tv_name.text = sendFood.namaMenu
                        view.tv_address.text = sendFood.alamat
                        view.tv_time.text = changeDateFormat(sendFood.waktuPengantaran, "yyyy-MM-dd HH:mm:ss", "HH:mm")
                        val difference = getDifferenceTime(changeDateFormat(sendFood.waktuPengantaran, "yyyy-MM-dd HH:mm:ss", "HH:mm"))
                        when {
                            difference >= 3 -> {
                                view.tv_time.text = getString(R.string.difference_time_text, difference.toString())
                                view.ic_indicator.setImageResource(R.color.Green)
                            }
                            difference >= 1 -> {
                                view.tv_time.text = getString(R.string.difference_time_text, difference.toString())
                                view.ic_indicator.setImageResource(R.color.Yellow)
                            }
                            difference > 0 -> {
                                view.tv_time.text = getString(R.string.less_than_one_hour)
                                view.ic_indicator.setImageResource(R.color.Red)
                            }
                            else -> {
                                view.tv_time.text = getString(R.string.late_text)
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
        sharedPreferenceUtil = SharedPreferenceUtil(activity)
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

        fab_send_food.setOnClickListener {
            activity?.let {
                if (!transaction) {
                    it.buildAlertDialog(getString(R.string.dialog_choose_transaction_title), getString(R.string.dialog_choice_transaction_detail), getString(R.string.dialog_ok), positiveAction = {
                        transaction = true
                        sendFoodAdapter.notifyDataSetChanged()
                        fab_send_food.setImageResource(R.drawable.ic_done_24dp)
                    }).show()
                } else {
                    if (listSendingFood.isEmpty()) {
                        activity?.showShortToast("Pilih makanan yang akan diantar")
                    } else {
                        transaction = false
                        sendFoodAdapter.notifyDataSetChanged()
                        fab_send_food.setImageResource(R.drawable.ic_add_24dp)
                        val intent = Intent(activity, SendFoodMapActivity::class.java)
                        intent.putParcelableArrayListExtra(LIST_SEND_FOOD, ArrayList(listSendingFood))
                        startActivityForResult(intent, SEND_FOOD)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEND_FOOD || resultCode == Activity.RESULT_OK) {
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