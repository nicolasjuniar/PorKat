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
import io.reactivex.functions.Function4
import juniar.porkat.R
import juniar.porkat.Utils.getAddress
import juniar.porkat.Utils.logDebug
import juniar.porkat.Utils.textToString
import juniar.porkat.auth.register.RegisterPelangganActivity.Companion.PRIVATE
import juniar.porkat.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_register_katering_private.*

/**
 * Created by Jarvis on 26/03/2018.
 */
class FillPrivateKateringFragment : BaseFragment<Any>() {

    lateinit var callback: RegisterKateringView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as RegisterKateringView
    }

    companion object {
        val PLACE_PICKER_REQUEST = 1
        fun newInstance(): FillPrivateKateringFragment {
            return FillPrivateKateringFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_register_katering_private, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Observable.combineLatest(
                RxTextView.textChanges(et_fullname)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_phone)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_address)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_verification)
                        .map { it.isNotEmpty() },
                Function4 { fullname: Boolean, phone: Boolean, address: Boolean, verification: Boolean ->
                    fullname && phone && address && verification })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    callback.onFieldFilled(it)
                }


        et_address.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()

            try {
                startActivityForResult(builder.build(activity), FillPrivatePelangganFragment.PLACE_PICKER_REQUEST)
            } catch (e: GooglePlayServicesRepairableException) {
                e.localizedMessage.logDebug()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.localizedMessage.logDebug()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == FillPrivatePelangganFragment.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(activity, data)
                val address = activity.getAddress(place.latLng.latitude, place.latLng.longitude)
                et_address.setText(address)
                callback.onPrivateFilled(et_fullname.textToString(), et_phone.textToString(), et_address.textToString(), et_verification.textToString())
            }
        }
    }

}