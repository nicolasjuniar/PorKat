package juniar.porkat.homepelanggan.setting

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.Toolbar
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.gson.Gson
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.PelangganModel
import juniar.porkat.auth.register.FillPrivatePelangganFragment
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_PELANGGAN
import kotlinx.android.synthetic.main.activity_edit_profile_pelanggan.*

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
class EditProfileActivity : BaseActivity<SettingPresenter>(), SettingView {

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    override fun onSetupLayout() {
        setContentView(R.layout.activity_edit_profile_pelanggan)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.edit_profile_text)
    }

    override fun onViewReady() {
        sharedPreferenceUtil = SharedPreferenceUtil(this@EditProfileActivity)
        presenter= SettingPresenter(this)
        val pelanggan = Gson().fromJson(sharedPreferenceUtil.getString(PROFILE_PELANGGAN), PelangganModel::class.java)
        Observable.combineLatest(
                RxTextView.textChanges(et_fullname)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_phone)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_address)
                        .map { it.isNotEmpty() },
                Function3 { fullname: Boolean, phone: Boolean, address: Boolean ->
                    fullname && phone && address
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    btn_update.setAvailable(it, this@EditProfileActivity)
                }

        RxTextView.textChanges(et_fullname)
                .map { alphabetOnly(it.toString()) || it.isEmpty() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        with(field_fullname)
                        {
                            error = null
                            isErrorEnabled = false
                        }
                        fullname.setTextColor(getColorCompat(R.color.hint_color))
                    } else {
                        with(field_fullname)
                        {
                            isErrorEnabled = true
                            error = getString(R.string.error_fullname_invalid).toHtmlText()
                        }
                        fullname.setTextColor(getColorCompat(R.color.md_red_500))
                    }
                }

        RxTextView.textChanges(et_phone)
                .map { it.isNotEmpty() && !isPhoneValid(it.toString()) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        with(field_phone) {
                            isErrorEnabled = true
                            error = getString(R.string.error_phone_invalid).toHtmlText()
                        }
                        et_phone.setTextColor(getColorCompat(R.color.md_red_500))
                    } else {
                        with(field_phone) {
                            error = null
                            isErrorEnabled = false
                        }
                        et_phone.setTextColor(getColorCompat(R.color.hint_color))
                    }
                }

        et_address.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()

            try {
                startActivityForResult(builder.build(this@EditProfileActivity), FillPrivatePelangganFragment.PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }
        setProfilePelanggan(pelanggan)

        btn_update.setOnClickListener {
            setLoading(true)
            presenter?.editProfilePelanggan(EditProfilePelangganRequest(pelanggan.idPelanggan,et_phone.textToString(),et_fullname.textToString(),et_address.textToString()))
        }
    }

    fun setProfilePelanggan(pelanggan: PelangganModel) {
        et_fullname.setText(pelanggan.namaLengkap)
        et_address.setText(pelanggan.alamat)
        et_phone.setText(pelanggan.noTelp)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == FillPrivatePelangganFragment.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(this@EditProfileActivity,data)
                val address = getAddress(place.latLng.latitude, place.latLng.longitude)
                et_address.setText(address)
            }
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

    override fun setLoading(loading: Boolean) {
        if(loading){
            window.setFlags(DONT_TOUCH, DONT_TOUCH)
            progressbar.show()
        }
        else{
            window.clearFlags(DONT_TOUCH)
            progressbar.hide()
        }
    }

}