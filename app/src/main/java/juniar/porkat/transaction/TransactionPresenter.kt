package juniar.porkat.transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Jarvis on 19/03/2018.
 */
class TransactionPresenter(val view: TransactionView) : BasePresenter() {

    fun orderKatering(request: TransaksiRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .orderKatering(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.onTransactionResponse(false, it.message, null) },
                        { view.onTransactionResponse(true, null, it) }
                ))
    }
}