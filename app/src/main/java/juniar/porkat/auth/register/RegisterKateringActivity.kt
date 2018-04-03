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
import juniar.porkat.common.Constant.CommonStrings.Companion.KATERING
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_KATERING
import juniar.porkat.common.Constant.CommonStrings.Companion.ROLE
import juniar.porkat.common.Constant.CommonStrings.Companion.SESSION
import juniar.porkat.homekatering.HomeKateringActivity
import kotlinx.android.synthetic.main.activity_register_pelanggan.*

/**
 * Created by Jarvis on 25/03/2018.
 */
class RegisterKateringActivity : BaseActivity<RegisterKateringPresenter>(), RegisterKateringView, ViewPager.OnPageChangeListener {

    val fragmentList = mutableListOf<Fragment>()
    lateinit var slider: SliderPagerAdapter
    var indicators = arrayListOf<ImageView>()
    var request = RegisterKateringRequest()
    val listBoolean = mutableListOf(false, false, true)
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    companion object {
        val AUTH = 0
        val PRIVATE = 1
        val PHOTO = 2
        val NUM_PAGES = 3
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_register_pelanggan)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.autentikasi_text)
    }

    override fun onViewReady() {
        presenter = RegisterKateringPresenter(this)
        sharedPreferenceUtil = SharedPreferenceUtil(this@RegisterKateringActivity)
        fragmentList.add(FillAuthKateringFragment.newInstance())
        fragmentList.add(FillPrivateKateringFragment.newInstance())
        fragmentList.add(SetKateringPhotoFragment.newInstance())
        slider = SliderPagerAdapter(supportFragmentManager, fragmentList)
        with(viewpager) {
            adapter = slider
            addOnPageChangeListener(this@RegisterKateringActivity)
        }
        setupPagerIndicator(NUM_PAGES)


    }

    override fun onBackPressed() {
        if (viewpager.currentItem != 0) {
            viewpager.currentItem--
            btn_register.setAvailable(listBoolean[viewpager.currentItem], this@RegisterKateringActivity)
        } else {
            super.onBackPressed()
        }
    }

    fun setLayout(index: Int) {
        when (index) {
            AUTH -> {
                changeTitleToolbar(R.string.autentikasi_text)
                with(btn_register) {
                    text = getString(R.string.next_text)
                    setOnClickListener {
                        viewpager.currentItem++
                        btn_register.setAvailable(listBoolean[viewpager.currentItem], this@RegisterKateringActivity)
                    }
                }
            }
            PRIVATE -> {
                changeTitleToolbar(R.string.private_text)
                with(btn_register) {
                    text = getString(R.string.next_text)
                    setOnClickListener {
                        viewpager.currentItem++
                        btn_register.setAvailable(listBoolean[viewpager.currentItem], this@RegisterKateringActivity)
                    }
                }
            }
            PHOTO -> {
                changeTitleToolbar(R.string.add_photo_text)
                with(btn_register) {
                    text = getString(R.string.register_text)
                    setOnClickListener {
                        setLoading(true)
                        presenter?.registerPelanggan(request)
                    }
                }
            }
        }
    }

    private fun setupPagerIndicator(size: Int) {

        for (i in 0 until size) {
            indicators.add(ImageView(this@RegisterKateringActivity))
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(this@RegisterKateringActivity,
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

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        setLayout(position)
    }

    override fun onPageSelected(position: Int) {
        for (i in 0 until NUM_PAGES) {
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(this@RegisterKateringActivity, R.drawable.dot_indicator_disabled))
        }
        indicators[position].setImageDrawable(
                ContextCompat.getDrawable(this@RegisterKateringActivity, R.drawable.dot_indicator))
    }

    override fun onFieldFilled(enable: Boolean) {
        listBoolean[viewpager.currentItem] = enable
        btn_register.setAvailable(enable, this@RegisterKateringActivity)
    }

    override fun onAuthFilled(username: String, password: String) {
        with(request) {
            idPengguna = username
            katasandi = password
        }
    }

    override fun onPrivateFilled(fullname: String, phone: String, address: String, verificationNumber: String, latitude: Double, longitude: Double) {
        with(request) {
            namaKatering = fullname
            noTelp = phone
            this.latitude = latitude
            this.longitude = longitude
            this.noVerifikasi=verificationNumber
            this.alamat=address
        }
    }

    override fun onGetPhotoKatering(photoName: String, encodedImage: String) {
        with(request) {
            foto = photoName
            this.encodedImage = encodedImage
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
                        setString(ROLE, KATERING)
                        setString(PROFILE_KATERING, it.dataKatering.encodeJson())
                    }
                    startActivity(Intent(this@RegisterKateringActivity, HomeKateringActivity::class.java))
                    finishAffinity()
                }
            }
        } else {
            t?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }
}
