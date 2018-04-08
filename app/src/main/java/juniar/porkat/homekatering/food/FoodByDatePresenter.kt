package juniar.porkat.homekatering.food

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

class FoodByDatePresenter(val view: FoodByDateView) : BasePresenter() {

    fun getListMakananToday(idKatering: Int) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .getListMakananToday(idKatering)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onGetListMakanan(false, it.listMenu, null)
                }, {
                    view.onGetListMakanan(true, null, it)
                }))
    }
}