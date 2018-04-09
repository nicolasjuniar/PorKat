package juniar.porkat.homekatering.transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

class KateringTransactionPresenter(val view: KateringTransactionView) : BasePresenter() {

    fun getListTransactionKatering(idKatering: Int) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .getListTransactionKatering(idKatering)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onGetListTransactionKatering(false, it.listTransaksi, null)
                }, {
                    view.onGetListTransactionKatering(true, null, it)
                }))
    }

}