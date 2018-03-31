package juniar.porkat.auth.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.register.RegisterPelangganActivity.Companion.AUTH
import juniar.porkat.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_autentikasi.*

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
class FillAuthPelangganFragment : BaseFragment<Any>() {

    lateinit var callback: RegisterPelangganView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as RegisterPelangganView
    }

    companion object {
        fun newInstance(): FillAuthPelangganFragment {
            return FillAuthPelangganFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_register_autentikasi, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Observable.combineLatest(
                RxTextView.textChanges(et_username)
                        .map { it.isNotEmpty() && it.length >= 4 },
                RxTextView.textChanges(et_password)
                        .map { it.isNotEmpty() && it.length >= 6 },
                BiFunction { username: Boolean, pass: Boolean ->
                    field_username.setErrorText(et_username.textToString().isEmpty() || username,getString(R.string.username_error_text))
                    tv_username.setErrorColor(et_username.textToString().isEmpty() || username,activity)

                    field_password.setErrorText(et_password.textToString().isEmpty() || pass, getString(R.string.password_error_text))
                    tv_password.setErrorColor(et_password.textToString().isEmpty() || pass, activity)

                    username && pass
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback.onFieldFilled(it,AUTH)
                    callback.onAuthFilled(et_username.textToString(),et_password.textToString())
                }
    }
}