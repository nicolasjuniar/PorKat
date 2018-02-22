package juniar.porkat.splashscreen

import android.content.Intent
import android.os.Handler
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.KATERING
import juniar.porkat.common.Constant.CommonStrings.Companion.PELANGGAN
import juniar.porkat.common.Constant.CommonStrings.Companion.ROLE
import juniar.porkat.common.Constant.CommonStrings.Companion.SESSION
import juniar.porkat.homepelanggan.HomePelangganActivity
import juniar.porkat.homescreen.HomeActivity

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class SplashScreenActivity : BaseActivity<Any>() {
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    override fun onSetupLayout() {
        setContentView(R.layout.activity_splashscreen)
    }

    override fun onViewReady() {
        sharedPreferenceUtil = SharedPreferenceUtil(this@SplashScreenActivity)
        Handler().postDelayed({
            loadPreferences()
            finish()
        }, 2000)
    }

    fun loadPreferences() {
        if (sharedPreferenceUtil.getBoolean(SESSION)) {
            val role = sharedPreferenceUtil.getString(ROLE)
            if (role.equals(PELANGGAN, true)) {
                startActivity(Intent(this@SplashScreenActivity, HomePelangganActivity::class.java))
            }
            if (role.equals(KATERING, true)) {
//                startActivity(Intent(this@SplashScreenActivity, MenuKateringActivity::class.java))
            }
        } else {
            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
        }
    }

}