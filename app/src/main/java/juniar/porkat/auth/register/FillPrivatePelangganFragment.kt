package juniar.porkat.auth.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.register.RegisterPelangganActivity.Companion.PRIVATE
import juniar.porkat.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_pelanggan_private.*

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
class FillPrivatePelangganFragment : BaseFragment<Any>() {

    lateinit var callback: RegisterPelangganView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as RegisterPelangganView
    }

    companion object {
        val PLACE_PICKER_REQUEST = 1
        fun newInstance(): FillPrivatePelangganFragment {
            return FillPrivatePelangganFragment()

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_register_pelanggan_private, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    callback.onFieldFilled(it, PRIVATE)
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
                        fullname.setTextColor(activity.getColorCompat(R.color.hint_color))
                        callback.onPrivateFilled(et_fullname.textToString(), et_phone.textToString(), et_address.textToString())
                    } else {
                        with(field_fullname)
                        {
                            isErrorEnabled = true
                            error = getString(R.string.error_fullname_invalid).toHtmlText()
                        }
                        fullname.setTextColor(activity.getColorCompat(R.color.md_red_500))
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
                        et_phone.setTextColor(activity.getColorCompat(R.color.md_red_500))
                    } else {
                        with(field_phone) {
                            error = null
                            isErrorEnabled = false
                        }
                        et_phone.setTextColor(activity.getColorCompat(R.color.hint_color))
                        callback.onPrivateFilled(et_fullname.textToString(), et_phone.textToString(), et_address.textToString())
                    }
                }

        et_address.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()

            try {
                startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.localizedMessage.logDebug()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.localizedMessage.logDebug()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(activity, data)
                val address = activity.getAddress(place.latLng.latitude, place.latLng.longitude)
                et_address.setText(address)
                callback.onPrivateFilled(et_fullname.textToString(), et_phone.textToString(), et_address.textToString())
            }
        }
    }
}