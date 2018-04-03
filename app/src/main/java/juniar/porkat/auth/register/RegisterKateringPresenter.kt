package juniar.porkat.auth.register

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Jarvis on 26/03/2018.
 */
class RegisterKateringPresenter(val view: RegisterKateringView) : BasePresenter() {
    fun registerPelanggan(request: RegisterKateringRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .registerKatering(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.onRegisterResponse(false, it, null) },
                        { view.onRegisterResponse(true, null, it) }
                ))
    }
}