package juniar.porkat.homepelanggan.transaction.invoice

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

class InvoicePresenter(val view: InvoiceView) : BasePresenter() {

    fun updateInvoice(idPesan: Int, nota: String, encodedImage: String) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .updateTransaction(idPesan, nota, encodedImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    view.onSuccessUpdateInvoice(false, it.message, null)
                }, {
                    view.onSuccessUpdateInvoice(true, null, it)
                }))
    }
}