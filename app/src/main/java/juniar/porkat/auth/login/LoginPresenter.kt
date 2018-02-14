package juniar.porkat.auth.login

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class LoginPresenter(val view: LoginView) : BasePresenter() {
    fun login(request: LoginRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .login(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.onLoginResponse(false, it, null) },
                        { view.onLoginResponse(true, null, it) }
                ))
    }
}