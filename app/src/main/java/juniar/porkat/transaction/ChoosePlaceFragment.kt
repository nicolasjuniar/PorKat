package juniar.porkat.transaction

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
import io.reactivex.android.schedulers.AndroidSchedulers
import juniar.porkat.R
import juniar.porkat.Utils.getAddress
import juniar.porkat.Utils.logDebug
import juniar.porkat.Utils.textToString
import juniar.porkat.auth.register.FillPrivatePelangganFragment.Companion.PLACE_PICKER_REQUEST
import juniar.porkat.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_choose_place.*

/**
 * Created by Jarvis on 18/03/2018.
 */
class ChoosePlaceFragment : BaseFragment<Any>() {

    lateinit var callback: TransactionView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as TransactionView
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_choose_place, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        RxTextView.textChanges(et_note)
                .map { it.toString() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback.onPickPlace(et_address.textToString(), et_note.textToString())
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(activity, data)
                val address = activity.getAddress(place.latLng.latitude, place.latLng.longitude)
                et_address.setText(address)
                callback.onPickPlace(et_address.textToString(), et_note.textToString())
            }
        }
    }
}