package juniar.porkat.homepelanggan.setting

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
class SettingPelangganPresenter(val view: SettingView) : BasePresenter() {

    fun editProfilePelanggan(request: EditProfilePelangganRequest) {
        NetworkManager.createService(NetworkApi::class.java)
                .editProfilePelanggan(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view.onUpdateProfile(false, it.message, null) },
                        { view.onUpdateProfile(true, null, it) }
                )
    }

    fun changePasswordPelanggan(request:ChangePasswordPelangganRequest){
        NetworkManager.createService(NetworkApi::class.java)
                .changePasswordPelanggan(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { view.onUpdateProfile(false, it.message, null) },
                        { view.onUpdateProfile(true, null, it) }
                )
    }
}