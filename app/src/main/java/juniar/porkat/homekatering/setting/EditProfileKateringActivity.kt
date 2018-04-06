package juniar.porkat.homekatering.setting

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
import juniar.porkat.auth.KateringModel
import juniar.porkat.auth.register.FillPrivatePelangganFragment.Companion.PLACE_PICKER_REQUEST
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_KATERING
import juniar.porkat.homepelanggan.setting.SettingPresenter
import juniar.porkat.homepelanggan.setting.SettingView
import kotlinx.android.synthetic.main.activity_edit_profile_katering.*

class EditProfileKateringActivity : BaseActivity<SettingPresenter>(), SettingView {

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    var longitude = 0.0
    var latitude = 0.0

    override fun onSetupLayout() {
        setContentView(R.layout.activity_edit_profile_katering)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.edit_profile_text)
    }

    override fun onViewReady() {
        sharedPreferenceUtil = SharedPreferenceUtil(this@EditProfileKateringActivity)
        val katering = Gson().fromJson(sharedPreferenceUtil.getString(PROFILE_KATERING), KateringModel::class.java)
        presenter = SettingPresenter(this)
        presenter?.setEditProfileValidation(Observable.combineLatest(
                RxTextView.textChanges(et_fullname)
                        .map { alphabetOnly(it.toString()) },
                RxTextView.textChanges(et_phone)
                        .map { isPhoneValid(it.toString()) },
                RxTextView.textChanges(et_address)
                        .map { it.isNotEmpty() },
                Function3 { fullname: Boolean, phone: Boolean, address: Boolean ->
                    field_fullname.setErrorText(fullname || et_fullname.textToString().isEmpty(), getString(R.string.error_fullname_invalid))
                    tv_katering_name.setErrorColor(fullname, this@EditProfileKateringActivity)

                    field_phone.setErrorText(phone || et_phone.textToString().isEmpty(), getString(R.string.error_phone_invalid))
                    tv_phone.setErrorColor(phone, this@EditProfileKateringActivity)
                    fullname && phone && address
                })
                .observeOn(AndroidSchedulers.mainThread()), {
            btn_update.setAvailable(it, this@EditProfileKateringActivity)
        })

        et_address.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()

            try {
                startActivityForResult(builder.build(this@EditProfileKateringActivity), PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
        }

        setProfileKatering(katering)

        btn_update.setOnClickListener {
            setLoading(true)
            presenter?.editProfileKatering(EditProfileKateringRequest(katering.idKatering, et_fullname.textToString(), et_phone.textToString(), et_address.textToString(), longitude, latitude))
        }
    }

    fun setProfileKatering(katering: KateringModel) {
        et_fullname.setText(katering.namaKatering)
        et_address.setText(katering.alamat)
        et_phone.setText(katering.noTelp)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val place = PlacePicker.getPlace(this@EditProfileKateringActivity, data)
            latitude = place.latLng.latitude
            longitude = place.latLng.longitude
            val address = getAddress(latitude, longitude)
            et_address.setText(address)
        }
    }


    override fun setLoading(loading: Boolean) {
        if (loading) {
            progressbar.show()
        } else {
            progressbar.hide()
        }
    }

    override fun onUpdateProfile(error: Boolean, message: String?, t: Throwable?) {
        if (!error) {
            message?.let {
                showShortToast(it)
            }
            finish()
        } else {
            t?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }
}