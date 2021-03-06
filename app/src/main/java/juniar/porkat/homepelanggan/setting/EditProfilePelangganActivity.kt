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
class EditProfilePelangganActivity : BaseActivity<SettingPresenter>(), SettingView {

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    lateinit var pelanggan: PelangganModel

    override fun onSetupLayout() {
        setContentView(R.layout.activity_edit_profile_pelanggan)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.edit_profile_text)
    }

    override fun onViewReady() {
        sharedPreferenceUtil = SharedPreferenceUtil(this@EditProfilePelangganActivity)
        presenter = SettingPresenter(this)
        pelanggan = Gson().fromJson(sharedPreferenceUtil.getString(PROFILE_PELANGGAN), PelangganModel::class.java)
        presenter?.setEditProfileValidation(Observable.combineLatest(
                RxTextView.textChanges(et_fullname)
                        .map { alphabetOnly(it.toString()) },
                RxTextView.textChanges(et_phone)
                        .map { isPhoneValid(it.toString()) },
                RxTextView.textChanges(et_address)
                        .map { it.isNotEmpty() },
                Function3 { fullname: Boolean, phone: Boolean, address: Boolean ->
                    field_fullname.setErrorText(fullname || et_fullname.textToString().isEmpty(), getString(R.string.error_fullname_invalid))
                    tv_fullname.setErrorColor(fullname, this@EditProfilePelangganActivity)

                    field_phone.setErrorText(phone || et_phone.textToString().isEmpty(), getString(R.string.error_phone_invalid))
                    tv_phone.setErrorColor(phone, this@EditProfilePelangganActivity)
                    fullname && phone && address
                })
                .observeOn(AndroidSchedulers.mainThread()), {
            btn_update.setAvailable(it, this@EditProfilePelangganActivity)
        })

        et_address.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()

            try {
                startActivityForResult(builder.build(this@EditProfilePelangganActivity), FillPrivatePelangganFragment.PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }
        setProfilePelanggan(pelanggan)

        btn_update.setOnClickListener {
            setLoading(true)
            presenter?.editProfilePelanggan(EditProfilePelangganRequest(pelanggan.idPelanggan, et_phone.textToString(), et_fullname.textToString(), et_address.textToString()))
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
                val place = PlacePicker.getPlace(this@EditProfilePelangganActivity, data)
                val address = getAddress(place.latLng.latitude, place.latLng.longitude)
                et_address.setText(address)
            }
        }
    }

    override fun onUpdateProfile(error: Boolean, message: String?, t: Throwable?) {
        if (!error) {
            message?.let {
                showShortToast(it)
            }
            pelanggan.namaLengkap = et_fullname.textToString()
            pelanggan.noTelp = et_phone.textToString()
            pelanggan.alamat = et_address.textToString()
            sharedPreferenceUtil.setString(PROFILE_PELANGGAN,pelanggan.encodeJson())
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            t?.let {
                showShortToast(it.localizedMessage)
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

}