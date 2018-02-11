package juniar.porkat.homescreen

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class KateringPresenter(val view:KateringView): BasePresenter(){
    fun getListKatering() {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .GetListKateringByRating()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.onGetListKatering(false, it, null) },
                        { view.onGetListKatering(true, null, it) }
                ))
    }
}