package juniar.porkat.homekatering

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

class EditKateringPresenter(val view:EditPhotoKateringView):BasePresenter(){

    fun updatePhotoKatering(request: UpdatePhotoKateringRequest){
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .updatePhotoKatering(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onSuccessUploadPhoto(false, it.message, null)
                }, {
                    view.onSuccessUploadPhoto(true, null, it)
                }))
    }
}