package juniar.porkat.homekatering.sendfood

import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.RetrofitMapsManager
import juniar.porkat.common.BasePresenter

class SendFoodMapPresenter(val view: SendFoodMapView) : BasePresenter() {

    fun getDirection(from: LatLng, to: LatLng) {
        compositeDisposable.add(RetrofitMapsManager.createService(NetworkApi::class.java)
                .getDirection(origin = "${from.latitude},${from.longitude}", destination = "${to.latitude},${to.longitude}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view.onGetDirection(false, it, null)
                }, {
                    view.onGetDirection(true, null, it)
                }))
    }

}