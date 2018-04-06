package juniar.porkat.homepelanggan.setting

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter
import juniar.porkat.homekatering.setting.ChangePasswordKateringRequest
import juniar.porkat.homekatering.setting.EditProfileKateringRequest

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
class SettingPresenter(val view: SettingView) : BasePresenter() {

    fun editProfilePelanggan(request: EditProfilePelangganRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .editProfilePelanggan(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view.onUpdateProfile(false, it.message, null) },
                        { view.onUpdateProfile(true, null, it) }
                ))
    }

    fun changePasswordPelanggan(request: ChangePasswordPelangganRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .changePasswordPelanggan(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view.onUpdateProfile(false, it.message, null) },
                        { view.onUpdateProfile(true, null, it) }
                ))
    }

    fun editProfileKatering(request: EditProfileKateringRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .editProfileKatering(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view.onUpdateProfile(false, it.message, null) },
                        { view.onUpdateProfile(true, null, it) }
                ))
    }

    fun changePasswordKatering(request: ChangePasswordKateringRequest) {
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .changePasswordKatering(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view.onUpdateProfile(false, it.message, null) },
                        { view.onUpdateProfile(true, null, it) }
                ))
    }

    fun setEditProfileValidation(validation: Observable<Boolean>, onValid: (valid: Boolean) -> Unit = {}) {
        compositeDisposable.add(validation
                .subscribe {
                    onValid.invoke(it)
                })
    }
}