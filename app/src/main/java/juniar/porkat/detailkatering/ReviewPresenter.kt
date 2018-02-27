package juniar.porkat.detailkatering

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
class ReviewPresenter(val view: ReviewView) : BasePresenter() {

    fun getListMenuKatering(idKatering: Int, idPelanggan: Int) {
        NetworkManager.createService(NetworkApi::class.java)
                .getListUlasan(idKatering, idPelanggan)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view.onGetListReview(false, it.listulasan, null) },
                        { view.onGetListReview(true, null, it) }
                )
    }
}