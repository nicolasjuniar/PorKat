package juniar.porkat.auth.register

import android.content.Intent
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
import juniar.porkat.common.Constant.CommonStrings.Companion.PELANGGAN
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_PELANGGAN
import juniar.porkat.common.Constant.CommonStrings.Companion.ROLE
import juniar.porkat.common.Constant.CommonStrings.Companion.SESSION
import juniar.porkat.homepelanggan.HomePelangganActivity
import kotlinx.android.synthetic.main.activity_register_pelanggan.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class RegisterPelangganActivity : BaseActivity<RegisterPelangganPresenter>(), RegisterPelangganView, ViewPager.OnPageChangeListener {

    val fragmentList = mutableListOf<Fragment>()
    lateinit var slider: SliderPagerAdapter
    var indicators = arrayListOf<ImageView>()
    var request = RegisterPelangganRequest()
    var buttonEnabled = false
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    companion object {
        val AUTH = 1
        val PRIVATE = 2
        val NUM_PAGES = 2
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_register_pelanggan)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.autentikasi_text)
    }

    override fun onViewReady() {
        presenter = RegisterPelangganPresenter(this)
        sharedPreferenceUtil = SharedPreferenceUtil(this@RegisterPelangganActivity)
        fragmentList.add(FillAuthPelangganFragment.newInstance())
        fragmentList.add(FillPrivatePelangganFragment.newInstance())
        slider = SliderPagerAdapter(supportFragmentManager, fragmentList)
        with(viewpager) {
            adapter = slider
            addOnPageChangeListener(this@RegisterPelangganActivity)
        }
        setupPagerIndicator(NUM_PAGES)


    }

    override fun onBackPressed() {
        if (viewpager.currentItem != 0) {
            viewpager.currentItem--
            btn_register.setAvailable(true, this@RegisterPelangganActivity)
        } else {
            super.onBackPressed()
        }
    }

    fun setLayout(index: Int) {
        if (index == 0) {
            changeTitleToolbar(R.string.autentikasi_text)
            with(btn_register) {
                text = getString(R.string.next_text)
                setOnClickListener {
                    btn_register.setAvailable(buttonEnabled, this@RegisterPelangganActivity)
                    viewpager.currentItem++
                }
            }
        } else {
            changeTitleToolbar(R.string.private_text)
            with(btn_register) {
                text = getString(R.string.register_text)
                setOnClickListener {
                    setLoading(true)
                    presenter?.registerPelanggan(request)
                }
            }
        }
    }

    private fun setupPagerIndicator(size: Int) {

        for (i in 0 until size) {
            indicators.add(ImageView(this@RegisterPelangganActivity))
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(this@RegisterPelangganActivity,
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

    override fun onRegisterResponse(error: Boolean, response: RegisterResponse?, t: Throwable?) {
        setLoading(false)
        if (!error) {
            response?.let {
                showShortToast(it.message)
                if (it.success) {
                    with(sharedPreferenceUtil) {
                        setBoolean(SESSION, true)
                        setString(ROLE, PELANGGAN)
                        setString(PROFILE_PELANGGAN, it.dataPelanggan.encodeJson())
                    }
                    startActivity(Intent(this@RegisterPelangganActivity, HomePelangganActivity::class.java))
                    finishAffinity()
                }
            }
        } else {
            showShortToast(t?.localizedMessage!!)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        setLayout(position)
    }

    override fun onPageSelected(position: Int) {
        for (i in 0 until NUM_PAGES) {
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this@RegisterPelangganActivity, R.drawable.dot_indicator_disabled))
        }
        indicators[position].setImageDrawable(ContextCompat.getDrawable(this@RegisterPelangganActivity, R.drawable.dot_indicator))
    }

    override fun onFieldFilled(enable: Boolean, code: Int) {
        if (code == PRIVATE) buttonEnabled = enable
        btn_register.setAvailable(enable, this@RegisterPelangganActivity)
    }

    override fun onAuthFilled(username: String, password: String) {
        with(request) {
            idPengguna = username
            katasandi = password
        }
    }

    override fun onPrivateFilled(fullname: String, phone: String, address: String) {
        with(request) {
            namaLengkap = fullname
            noTelp = phone
            alamat = address
        }
    }
}