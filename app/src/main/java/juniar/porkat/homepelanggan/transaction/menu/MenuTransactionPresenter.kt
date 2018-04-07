package juniar.porkat.homepelanggan.transaction.menu

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

class MenuTransactionPresenter(val view:MenuTransactionView):BasePresenter(){
    fun getListMenuTransaction(idPesan: Int) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .getMenuTransaction(idPesan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onGetListMenuTransaction(false, it.listMenu, null)
                }, {
                    view.onGetListMenuTransaction(true, null, it)
                }))
    }
}