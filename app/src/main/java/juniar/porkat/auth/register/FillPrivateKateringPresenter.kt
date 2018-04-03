package juniar.porkat.auth.register

import io.reactivex.Observable
import juniar.porkat.common.BasePresenter

class FillPrivateKateringPresenter : BasePresenter() {
    fun setPrivateKateringDataValidation(validation: Observable<Boolean>, onValid: (valid: Boolean) -> Unit = {}) {
        compositeDisposable.add(validation
                .subscribe {
                    onValid.invoke(it)
                })
    }
}