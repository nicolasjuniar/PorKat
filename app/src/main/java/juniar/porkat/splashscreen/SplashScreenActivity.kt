package juniar.porkat.splashscreen

import android.content.Intent
import android.util.Log
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.common.BaseActivity
import juniar.porkat.homescreen.HomeActivity

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class SplashScreenActivity : BaseActivity<Any>() {
    lateinit var prefs: SharedPreferenceUtil

    override fun onSetupLayout() {
        setContentView(R.layout.activity_splashscreen)
    }

    override fun onViewReady() {
        prefs = SharedPreferenceUtil(this@SplashScreenActivity)
        object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    Log.d("Exception", "Exception" + e)
                } finally {
                    loadPreferences()
                }
                finish()
            }
        }.start()
    }

    fun loadPreferences() {
        if (prefs.getBoolean("login")) {
            val role = prefs.getString("role")
            if (role.equals("pelanggan", true)) {
//                startActivity(Intent(this@SplashScreenActivity, MenuPelangganActivity::class.java))
            }
            if (role.equals("katering", true)) {
//                startActivity(Intent(this@SplashScreenActivity, MenuKateringActivity::class.java))
            }
        } else {
            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
        }
    }

}