package juniar.porkat.homekatering.sendfood

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

class SendFoodPresenter(val view: SendFoodView) : BasePresenter() {

    fun getListSendFood(idKatering: Int) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .getListSendFood(idKatering)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onGetListSendFood(false, it.listTransaksi, null)
                }, {
                    view.onGetListSendFood(true, null, it)
                }))
    }
}