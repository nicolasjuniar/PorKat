package juniar.porkat.common

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
abstract class BasePresenter {
    protected val compositeDisposable = CompositeDisposable()

    fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }
}