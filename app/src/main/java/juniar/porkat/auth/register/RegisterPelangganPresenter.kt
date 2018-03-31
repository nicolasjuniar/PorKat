package juniar.porkat.auth.register

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
class RegisterPelangganPresenter(val pelangganView: RegisterPelangganView) : BasePresenter() {
    fun registerPelanggan(request: RegisterPelangganRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .registerPelanggan(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { pelangganView.onRegisterResponse(false, it, null) },
                        { pelangganView.onRegisterResponse(true, null, it) }
                ))
    }
}