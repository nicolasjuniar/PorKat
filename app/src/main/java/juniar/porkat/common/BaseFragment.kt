package juniar.porkat.common

import android.support.v4.app.Fragment

/**
 * Created by voen on 13/02/18.
 */
abstract class BaseFragment<T> : Fragment() {
    protected var presenter: T? = null

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.let {
            (it as BasePresenter).clearCompositeDisposable()
        }
    }
}