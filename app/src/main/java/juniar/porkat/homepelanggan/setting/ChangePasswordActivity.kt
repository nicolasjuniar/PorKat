package juniar.porkat.homepelanggan.setting

import android.support.v7.widget.Toolbar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.PelangganModel
import juniar.porkat.common.BaseActivity
import kotlinx.android.synthetic.main.activity_change_password.*

/**
 * Created by Nicolas Juniar on 23/02/2018.
 */
class ChangePasswordActivity : BaseActivity<SettingPelangganPresenter>(), SettingView {

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    lateinit var pelanggan:PelangganModel

    override fun onSetupLayout() {
        setContentView(R.layout.activity_change_password)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.change_password_text)
    }

    override fun onViewReady() {
        presenter= SettingPelangganPresenter(this)
        sharedPreferenceUtil=SharedPreferenceUtil(this@ChangePasswordActivity)
        Observable.combineLatest(
                RxTextView.textChanges(et_old_password)
                        .map { it.isNotEmpty() && it.length >= 6 },
                RxTextView.textChanges(et_new_password)
                        .map { it.isNotEmpty() && it.length >= 6 },
                BiFunction { oldPass: Boolean, newPass: Boolean ->
                    oldPass && newPass
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    btn_update.setAvailable(it, this@ChangePasswordActivity)
                }

        RxTextView.textChanges(et_old_password)
                .map { it.isNotEmpty() && it.length < 6 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        with(field_old_password)
                        {
                            isErrorEnabled = true
                            error = getString(R.string.password_error_text).toHtmlText()
                        }
                        old_password.setTextColor(getColorCompat(R.color.md_red_500))
                    } else {
                        with(field_old_password)
                        {
                            error = null
                            isErrorEnabled = false
                        }
                        old_password.setTextColor(getColorCompat(R.color.hint_color))
                    }
                }

        RxTextView.textChanges(et_new_password)
                .map { it.isNotEmpty() && it.length < 6 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        with(field_new_password)
                        {
                            isErrorEnabled = true
                            error = getString(R.string.password_error_text).toHtmlText()
                        }
                        new_password.setTextColor(getColorCompat(R.color.md_red_500))
                    } else {
                        with(field_new_password)
                        {
                            error = null
                            isErrorEnabled = false
                        }
                        new_password.setTextColor(getColorCompat(R.color.hint_color))
                    }
                }
        pelanggan= getProfilePelanggan(sharedPreferenceUtil)
        btn_update.setOnClickListener {
            if(et_old_password.textToString() != pelanggan.katasandi){
                showShortToast(getString(R.string.old_password_invalid))
            }
            else{
                setLoading(true)
                presenter?.changePasswordPelanggan(ChangePasswordPelangganRequest(pelanggan.id_pelanggan,et_new_password.textToString()))
            }
        }
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            window.setFlags(DONT_TOUCH, DONT_TOUCH)
            progressbar.show()
        } else {
            window.clearFlags(DONT_TOUCH)
            progressbar.hide()
        }
    }

    override fun onUpdateProfile(error: Boolean, message: String?, t: Throwable?) {
        if (!error) {
            showShortToast(message!!)
            finish()
        } else {
            showShortToast(t?.localizedMessage!!)
        }
    }

}