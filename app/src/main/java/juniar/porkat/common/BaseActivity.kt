package juniar.porkat.common

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.logDebug
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.Constant.CommonStrings.Companion.EMPTY_STRING
import juniar.porkat.common.Constant.CommonStrings.Companion.LATITUDE
import juniar.porkat.common.Constant.CommonStrings.Companion.LONGITUDE
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
abstract class BaseActivity<T> : AppCompatActivity() {

    var presenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSetupLayout()
        onViewReady()
    }

    fun setupToolbarTitle(toolbarId: Toolbar, title: Int = R.string.empty_string, drawable: Int = R.drawable.ic_back_24dp) {
        setSupportActionBar(toolbarId)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setHomeAsUpIndicator(drawable)
        }
        toolbar_title.setText(title)
    }

    fun setupToolbarTitle(toolbarId: Toolbar, title: String = EMPTY_STRING, drawable: Int = R.drawable.ic_back_24dp) {
        setSupportActionBar(toolbarId)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setHomeAsUpIndicator(drawable)
        }
        toolbar_title.text = title
    }

    fun changeTitleToolbar(title: String) {
        toolbar_title.setText(title)
    }

    fun changeTitleToolbar(title: Int) {
        toolbar_title.setText(title)
    }

    fun setMyLocation(sharedPreferenceUtil: SharedPreferenceUtil) {
        val locationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: Location) {
                sharedPreferenceUtil.setString(LONGITUDE, location.longitude.toString())
                sharedPreferenceUtil.setString(LATITUDE, location.latitude.toString())
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            when{
                locationManager.allProviders.contains(LocationManager.NETWORK_PROVIDER)->{
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
                }
                locationManager.allProviders.contains(LocationManager.GPS_PROVIDER)->{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
                }
            }
        } catch (e: SecurityException) {
            e.localizedMessage.logDebug()
        }
    }

    fun setupToolbarTitleNoBack(toolbarId: Toolbar, title: Int = R.string.empty_string) {
        setSupportActionBar(toolbarId)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
        }
        toolbar_title.setText(title)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.let {
            (presenter as BasePresenter).clearCompositeDisposable()
        }
    }

    protected abstract fun onSetupLayout()
    protected abstract fun onViewReady()
}
