package juniar.porkat.common

import android.location.Location
import android.support.v4.app.Fragment
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import juniar.porkat.R
import juniar.porkat.Utils.MyLocation
import juniar.porkat.Utils.SharedPreferenceUtil

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

    fun getMyLocation(sharedPreferenceUtil: SharedPreferenceUtil) {
        val locationResult = object : MyLocation.LocationResult() {
            override fun gotLocation(location: Location) {
                val loc = LatLng(location.latitude, location.longitude)
                sharedPreferenceUtil.setString(Constant.CommonStrings.LONGITUDE, loc.longitude.toString())
                sharedPreferenceUtil.setString(Constant.CommonStrings.LATITUDE, loc.latitude.toString())
            }
        }
        val myLocation = MyLocation(activity)
        myLocation.getLocation(activity, locationResult)
    }
}