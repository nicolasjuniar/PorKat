package juniar.porkat.homekatering.menu

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

class EditMenuPresenter(val view: EditMenuView) : BasePresenter() {

    fun insertMenu(request: InsertMenuRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .insertMenu(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onInsertMenu(false, it.message, null)
                }, {
                    view.onInsertMenu(true, null, it)
                }))
    }

    fun deleteMenu(request: DeleteMenuRequest){
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .deleteMenu(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onInsertMenu(false, it.message, null)
                }, {
                    view.onInsertMenu(true, null, it)
                }))
    }

    fun updateMenu(request: UpdateMenuRequest){
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .updateMenu(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onInsertMenu(false, it.message, null)
                }, {
                    view.onInsertMenu(true, null, it)
                }))
    }

    fun setMenuValidation(validation:Observable<Boolean>,onValid:(valid:Boolean)->Unit={}){
        compositeDisposable.add(validation
                .subscribe {
                    onValid.invoke(it)
                })
    }
}