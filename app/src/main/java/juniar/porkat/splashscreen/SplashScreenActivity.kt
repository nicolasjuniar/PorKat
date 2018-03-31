package juniar.porkat.splashscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.checkRequestPermission
import juniar.porkat.Utils.makeRequest
import juniar.porkat.Utils.sdkVersion
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonInt.Companion.ACCESS_FINE_LOCATION_CODE
import juniar.porkat.common.Constant.CommonStrings.Companion.KATERING
import juniar.porkat.common.Constant.CommonStrings.Companion.PELANGGAN
import juniar.porkat.common.Constant.CommonStrings.Companion.ROLE
import juniar.porkat.common.Constant.CommonStrings.Companion.SESSION
import juniar.porkat.homekatering.HomeKateringActivity
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
        if (sdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Handler().postDelayed({
                    setMyLocation(sharedPreferenceUtil)
                    loadPreferences()
                    finish()
                }, 2000)
            } else {
                makeRequest(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION_CODE)
            }
        } else {
            makeRequest(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION_CODE)
        }
    }

    fun loadPreferences() {
        if (sharedPreferenceUtil.getBoolean(SESSION)) {
            val role = sharedPreferenceUtil.getString(ROLE)
            if (role.equals(PELANGGAN, true)) {
                startActivity(Intent(this@SplashScreenActivity, HomePelangganActivity::class.java))
            }
            if (role.equals(KATERING, true)) {
                startActivity(Intent(this@SplashScreenActivity, HomeKateringActivity::class.java))
            }
        } else {
            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_FINE_LOCATION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setMyLocation(sharedPreferenceUtil)
                }
                Handler().postDelayed({
                    loadPreferences()
                    finish()
                }, 2000)
            }
        }
    }

}