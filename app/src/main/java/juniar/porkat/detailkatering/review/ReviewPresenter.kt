package juniar.porkat.detailkatering.review

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
class ReviewPresenter(val view: ReviewView? = null, val view2: ReviewDialogView? = null) : BasePresenter() {

    fun getListMenuKatering(idKatering: Int, idPelanggan: Int) {
        NetworkManager.createService(NetworkApi::class.java)
                .getListUlasan(idKatering, idPelanggan)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view!!.onGetReview(false, it, null) },
                        { view!!.onGetReview(true, null, it) }
                )
    }

    fun deleteReview(idUlasan: Int) {
        NetworkManager.createService(NetworkApi::class.java)
                .deleteUlasan(idUlasan)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view!!.onDeleteReview(false, it.message, null) },
                        { view!!.onDeleteReview(true, null, it) }
                )
    }

    fun insertReview(request: InsertReviewRequest) {
        NetworkManager.createService(NetworkApi::class.java)
                .insertUlasan(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view2!!.onInsertReview(false, it, null) },
                        { view2!!.onInsertReview(true, null, it) }
                )
    }

    fun updateReview(request: UpdateReviewRequest) {
        NetworkManager.createService(NetworkApi::class.java)
                .updateUlasan(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view2!!.onUpdateReview(false, it, null) },
                        { view2!!.onUpdateReview(true, null, it) }
                )
    }
}