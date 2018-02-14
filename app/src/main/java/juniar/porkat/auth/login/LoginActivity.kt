package juniar.porkat.login

import android.content.Intent
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.Toolbar
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.common.BaseActivity
import juniar.porkat.homepelanggan.HomePelangganActivity
import juniar.porkat.register.RegisterPelangganActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginView {
    lateinit var prefs: SharedPreferenceUtil

    override fun onSetupLayout() {
        setContentView(R.layout.activity_login)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.login_text)
    }

    override fun onViewReady() {
        prefs = SharedPreferenceUtil(this@LoginActivity)
        presenter = LoginPresenter(this@LoginActivity)

        val str2 = getString(R.string.register_here)
        val str1 = getString(R.string.register_reminder, str2)
        val start = str1.indexOf(str2)
        val end = start + str2.length

        Observable.combineLatest(
                RxTextView.textChanges(et_username)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_password)
                        .map { it.isNotEmpty() },
                BiFunction { username: Boolean, password: Boolean ->
                    username && password
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { btn_login.setAvailable(it, this@LoginActivity) }

        val spanBuilder = SpannableStringBuilder(str1)
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View?) {
                startActivity(Intent(this@LoginActivity, RegisterPelangganActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint?) {
                super.updateDrawState(ds)
                ds?.let {
                    it.isUnderlineText = false
                    it.color = getColorCompat(R.color.colorPrimary)
                    it.typeface = ResourcesCompat.getFont(this@LoginActivity, R.font.roboto_bold)
                }
            }
        }
        spanBuilder.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        with(tv_register) {
            text = spanBuilder
            highlightColor = android.graphics.Color.TRANSPARENT
            movementMethod = android.text.method.LinkMovementMethod.getInstance()
        }

        btn_login.setOnClickListener {
            setLoading(true)
            presenter?.login(LoginRequest(et_username.text.toString(), et_password.text.toString()))
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

    override fun onLoginResponse(error: Boolean, response: LoginResponse?, t: Throwable?) {
        setLoading(false)
        if (!error) {
            showShortToast(response?.message!!)
            if(response?.success!!){
                startActivity(Intent(this@LoginActivity, HomePelangganActivity::class.java))
                finishAffinity()
            }
        } else {
            showShortToast(t?.localizedMessage!!)
        }
    }

}