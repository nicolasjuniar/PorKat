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
import juniar.porkat.Utils.getColorCompat
import juniar.porkat.Utils.toHtmlText
import juniar.porkat.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_autentikasi.*
import java.util.concurrent.TimeUnit

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
class FillAuthPelangganFragment : BaseFragment<Any>() {

    lateinit var callback: RegisterView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as RegisterView
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
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_password)
                        .map { it.isNotEmpty() && it.length >= 6 },
                BiFunction { username: Boolean, pass: Boolean ->
                    username && pass
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback.onFieldFilled(it)
                }

        RxTextView.textChanges(et_username)
                .debounce(1,TimeUnit.SECONDS)
                .map { it.isNotEmpty() && it.length<4 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        with(field_username) {
                            isErrorEnabled = true
                            error = getString(R.string.username_error_text).toHtmlText()
                        }
                        username.setTextColor(activity.getColorCompat(R.color.md_red_500))
                    } else {
                        with(field_username) {
                            error = null
                            isErrorEnabled = false
                        }
                        username.setTextColor(activity.getColorCompat(R.color.hint_color))
                    }
                }

        RxTextView.textChanges(et_password)
                .debounce(1,TimeUnit.SECONDS)
                .map { it.isNotEmpty() && it.length<6 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        with(field_password)
                        {
                            isErrorEnabled = true
                            error = getString(R.string.password_error_text).toHtmlText()
                        }
                        password.setTextColor(activity.getColorCompat(R.color.md_red_500))
                    } else {
                        with(field_password)
                        {
                            error = null
                            isErrorEnabled = false
                        }
                        password.setTextColor(activity.getColorCompat(R.color.hint_color))
                    }
                }
    }
}