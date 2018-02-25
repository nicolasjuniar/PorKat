package juniar.porkat.detailkatering

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Nicolas Juniar on 26/02/2018.
 */
class MenuPresenter(val view: MenuView) : BasePresenter() {

    fun getListMenuKatering(idKatering: Int) {
        NetworkManager.createService(NetworkApi::class.java)
                .getListMenu(idKatering)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view.onGetListMenu(false, it.listMenu, null) },
                        { view.onGetListMenu(true, null, it) }
                )
    }
}
