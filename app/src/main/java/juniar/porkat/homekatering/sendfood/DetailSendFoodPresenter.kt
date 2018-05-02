package juniar.porkat.homekatering.sendfood

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import juniar.porkat.Utils.NetworkApi
import juniar.porkat.Utils.NetworkManager
import juniar.porkat.common.BasePresenter

class DetailSendFoodPresenter(val view:DetailSendFoodView):BasePresenter(){

    fun sendFood(idDetailPesan:Int){
        compositeDisposable.add(NetworkManager.createService(NetworkApi::class.java)
                .doneSendFood(idDetailPesan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onDoneSendFood(false, it.message,null)
                },{
                    view.onDoneSendFood(true,null,it)
                }))
    }
}