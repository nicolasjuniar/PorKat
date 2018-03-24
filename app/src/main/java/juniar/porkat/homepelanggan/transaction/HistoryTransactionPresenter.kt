package juniar.porkat.homepelanggan.transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Jarvis on 22/03/2018.
 */

class HistoryTransactionPresenter(val view: HistoryTransactionView) : BasePresenter() {

    fun getListTransactionPelanggan(idPelanggan: Int) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .getListTransactionPelanggan(idPelanggan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.onGetListTransactionPelanggan(false, it.listtransaksi, null) },
                        { view.onGetListTransactionPelanggan(true, null, it) }
                ))
    }

}