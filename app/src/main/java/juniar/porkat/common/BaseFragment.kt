package juniar.porkat.common

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.logDebug
import juniar.porkat.Utils.showShortToast

/**
 * Created by voen on 13/02/18.
 */
abstract class BaseFragment<T> : Fragment() {
    protected var presenter: T? = null

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.let {
            (it as BasePresenter).clearCompositeDisposable()
        }
    }

    fun setMyLocation(sharedPreferenceUtil: SharedPreferenceUtil) {
        val locationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: Location) {
                sharedPreferenceUtil.setString(Constant.CommonStrings.LONGITUDE, location.longitude.toString())
                sharedPreferenceUtil.setString(Constant.CommonStrings.LATITUDE, location.latitude.toString())
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L,
                    0f, locationListener)
        } catch (e: SecurityException) {
            e.localizedMessage.logDebug()
        }
    }
}