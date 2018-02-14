package id.co.binar.binarapp.base

import android.support.v4.app.Fragment
import id.co.binar.binarapp.BinarApp

/**
 * Created by voen on 13/02/18.
 */
abstract class BaseFragment<T>: Fragment() {
    protected var presenter: T? = null
    protected val appComponent by lazy {
        (activity?.application as BinarApp).appComponent
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.let {
            (it as BasePresenter).clearCompositeDisposable()
        }
    }
}