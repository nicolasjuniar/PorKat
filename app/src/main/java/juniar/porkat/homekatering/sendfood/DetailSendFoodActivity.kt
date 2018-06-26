package juniar.porkat.homekatering.sendfood

import android.app.Activity
import android.support.v7.widget.Toolbar
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.common.BaseActivity
import kotlinx.android.synthetic.main.activity_detail_send_food.*

class DetailSendFoodActivity : BaseActivity<DetailSendFoodPresenter>(), DetailSendFoodView {

    companion object {
        const val DETAIL_SEND_FOOD = "detail_send_food"
        const val DETAIL_SEND_FOOD_CODE = 1
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_detail_send_food)
        setupToolbarTitle(toolbar_layout as Toolbar, getString(R.string.detail_send_food_text))
    }

    override fun onViewReady() {
        presenter = DetailSendFoodPresenter(this)
        val sendFood = intent.extras.get(DETAIL_SEND_FOOD) as SendFoodModel
        tv_fullname.text = sendFood.namaLengkap
        tv_address.text = sendFood.alamat
        tv_time.text = changeDateFormat(sendFood.waktuPengantaran,"yyyy-MM-dd HH:mm:ss","HH:mm")
        tv_menu.text = sendFood.namaMenu
        tv_note.text = sendFood.catatan
        if (sendFood.status == getString(R.string.done_send_text)) {
            btn_done.hide()
        }
        btn_done.setOnClickListener {
            buildAlertDialog(getString(R.string.dialog_done_send_food_title),getString(R.string.dialog_done_send_food_detail),getString(R.string.yes_dialog),getString(R.string.no_dialog),positiveAction = {
                setLoading(true)
                presenter?.sendFood(sendFood.idDetailPesan)
            }).show()
        }
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            window.setFlags(DONT_TOUCH, DONT_TOUCH)
            pb_loading.show()
        } else {
            window.clearFlags(DONT_TOUCH)
            pb_loading.hide()
        }
    }

    override fun onDoneSendFood(error: Boolean, message: String?, throwable: Throwable?) {
        setLoading(false)
        if (!error) {
            message?.let {
                showShortToast(it)
            }
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            throwable?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }

}