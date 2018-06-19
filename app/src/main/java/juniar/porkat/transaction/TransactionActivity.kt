package juniar.porkat.transaction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.common.BaseActivity
import juniar.porkat.detailkatering.menu.MenuFragment.Companion.ID_KATERING
import kotlinx.android.synthetic.main.activity_transaction.*
import org.joda.time.DateTime

/**
 * Created by Jarvis on 12/03/2018.
 */
class TransactionActivity : BaseActivity<TransactionPresenter>(), ViewPager.OnPageChangeListener, TransactionView {

    val fragmentList = mutableListOf<Fragment>()
    lateinit var slider: SliderPagerAdapter
    lateinit var pickMenuFragment: PickMenuFragment
    var indicators = arrayListOf<ImageView>()
    var listBoolean = mutableListOf(false, false, false)
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    val listTitle by lazy {
        mutableListOf(getString(R.string.description_transaction),
                getString(R.string.pick_menu),
                getString(R.string.choose_place)
        )
    }

    var listDetailTransaksi = mutableListOf<DetailTransaksiModel>()
    var listPickMenuModel = mutableListOf<PickMenuModel>()
    var transaksiRequest = TransactionRequest(detailPesan = listDetailTransaksi)
    var transactionNumber = -1
    var orderDay = -1

    companion object {
        val PICK_MENU = 1
        val CHOOSE_PLACE = 2
        val NUM_PAGES = 3
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_transaction)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.description_transaction)
    }

    override fun onViewReady() {
        val fragmentPickMenu = PickMenuFragment()
        val bundle = Bundle()
        val idKatering = intent.getIntExtra(ID_KATERING, -1)
        presenter = TransactionPresenter(this)
        sharedPreferenceUtil = SharedPreferenceUtil(this@TransactionActivity)
        bundle.putInt(ID_KATERING, idKatering)
        fragmentPickMenu.arguments = bundle
        fragmentList.add(DescriptionTransactionFragment())
        fragmentList.add(fragmentPickMenu)
        fragmentList.add(ChoosePlaceFragment())
        btn_submit.setAvailable(listBoolean[viewpager.currentItem], this@TransactionActivity)
        slider = SliderPagerAdapter(supportFragmentManager, fragmentList)
        with(viewpager) {
            adapter = slider
            addOnPageChangeListener(this@TransactionActivity)
        }
        pickMenuFragment = (viewpager.adapter as SliderPagerAdapter).getItem(PICK_MENU) as PickMenuFragment
        setupPagerIndicator(NUM_PAGES)
        transaksiRequest.transaksiModel.idKatering = idKatering
        transaksiRequest.transaksiModel.idPelanggan = getProfilePelanggan(sharedPreferenceUtil).idPelanggan
        btn_submit.setOnClickListener {
            if (viewpager.currentItem != CHOOSE_PLACE) {
                viewpager.currentItem++
                btn_submit.setAvailable(listBoolean[viewpager.currentItem], this@TransactionActivity)
                changeTitleToolbar(listTitle[viewpager.currentItem])
                if (viewpager.currentItem == CHOOSE_PLACE) {
                    btn_submit.text = getString(R.string.order_text)
                }
            } else {
                doOrderKatering(listPickMenuModel)
            }
        }
    }

    override fun onBackPressed() {
        if (viewpager.currentItem != 0) {
            viewpager.currentItem--
            changeTitleToolbar(listTitle[viewpager.currentItem])
            btn_submit.text = getString(R.string.next_text)
            btn_submit.setAvailable(listBoolean[viewpager.currentItem], this@TransactionActivity)
        } else {
            buildAlertDialog(getString(R.string.dialog_exit_transaction_title), getString(R.string.dalog_exit_transaction_detail), getString(R.string.yes_dialog), getString(R.string.no_dialog), {
                super.onBackPressed()
            }).show()
        }
    }

    private fun setupPagerIndicator(size: Int) {
        for (i in 0 until size) {
            indicators.add(ImageView(this@TransactionActivity))
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(this@TransactionActivity,
                            if (i == 0) R.drawable.dot_indicator else R.drawable.dot_indicator_disabled)
            )

            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.LEFT
            }

            val margin = resources.getDimensionPixelSize(R.dimen.dimen_3dp)
            params.setMargins(
                    if (i != 0) margin else 0,
                    0,
                    margin,
                    0
            )
            layout_indicator.addView(indicators[i], params)
        }
        indicators[0].isSelected = true
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            window.setFlags(DONT_TOUCH, DONT_TOUCH)
            progressbar.show()
        } else {
            window.clearFlags(DONT_TOUCH)
            progressbar.hide()
        }
    }

    override fun onGetDescriptionTransaction(startDate: String, orderDay: Int, transactionNumber: Int) {
        listBoolean[viewpager.currentItem] = startDate.isNotEmpty() && orderDay != -1 && transactionNumber != -1
        btn_submit.setAvailable(listBoolean[viewpager.currentItem], this@TransactionActivity)
        this.orderDay = orderDay
        this.transactionNumber = transactionNumber - 1
        pickMenuFragment.changeAdapterSize(transactionNumber - 1)
        if (startDate.isNotEmpty()) {
            transaksiRequest.transaksiModel.tglMulai = changeDateFormat(startDate, "d MMMM yyyy", "yyyy-MM-dd")
            transaksiRequest.transaksiModel.tglSelesai = getDateTimeAnotherFormat(startDate, "d MMMM yyyy", "yyyy-MM-dd").plusDays(orderDay).toString("yyyy-MM-dd")
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        for (i in 0 until NUM_PAGES) {
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this@TransactionActivity, R.drawable.dot_indicator_disabled))
        }
        indicators[position].setImageDrawable(ContextCompat.getDrawable(this@TransactionActivity, R.drawable.dot_indicator))
    }

    override fun onPickMenu(list: MutableList<PickMenuModel>) {
        var cek = false
        list.forEachIndexed { index, pickMenuModel ->
            if (index <= transactionNumber) {
                cek = pickMenuModel.picked && pickMenuModel.delilveryTime.isNotEmpty()
            }
        }
        listPickMenuModel.clear()
        listPickMenuModel.addAll(list)
        listBoolean[viewpager.currentItem] = cek
        btn_submit.setAvailable(cek, this@TransactionActivity)
    }

    override fun onPickPlace(address: String, note: String, longitude: Double, latitude: Double) {
        listBoolean[viewpager.currentItem] = address.isNotEmpty()
        btn_submit.setAvailable(listBoolean[viewpager.currentItem], this@TransactionActivity)
        with(transaksiRequest.transaksiModel) {
            alamat = address
            this.longitude = longitude
            this.latitude = latitude
            catatan = note
        }
    }

    fun doOrderKatering(list: MutableList<PickMenuModel>) {
        var waktuPengantaran: String
        var listWaktuPengantaran = mutableListOf<DateTime>()
        var jumlahMenu = 0
        var total = 0
        list.forEach {
            if (it.visibility) {
                waktuPengantaran = "${transaksiRequest.transaksiModel.tglMulai} ${it.delilveryTime}:00"
                listWaktuPengantaran.add(waktuPengantaran.toDateTime("yyyy-MM-dd HH:mm:ss"))
                jumlahMenu++
                total += orderDay * it.menu.harga
            }
        }
        for (i in 0 until orderDay) {
            for (j in 0 until jumlahMenu) {
                transaksiRequest.detailPesan.add(DetailTransaksiModel(waktuPengantaran = listWaktuPengantaran[j].plusDays(i).toString("yyyy-MM-dd HH:mm:ss"), idMenu = list[j].menu.idMenu))
            }
        }
        transaksiRequest.transaksiModel.total = total
        setLoading(true)
        presenter?.orderKatering(transaksiRequest)
    }


    override fun onTransactionResponse(error: Boolean, message: String?, t: Throwable?) {
        setLoading(false)
        if (error) {
            t?.let {
                it.localizedMessage.logDebug()
            }
        } else {
            message?.let {
                showShortToast(it)
            }
            finish()
        }
    }

}