package juniar.porkat.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.Utils.changeDateFormat
import juniar.porkat.Utils.getMonth
import juniar.porkat.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_description_transaction.*
import java.util.*

/**
 * Created by Jarvis on 12/03/2018.
 */
class DescriptionTransactionFragment : BaseFragment<Any>() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_description_transaction, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSwitchClick()

        var calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(context, DatePickerDialog.OnDateSetListener
        { _, year, month, day ->
            et_start_date.setText(changeDateFormat("$day ${getMonth(month)} $year", "d MMMM yyyy", "d MMMM yyyy"))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

        et_start_date.setOnClickListener {
            datePicker.show()
        }
    }

    fun setSwitchClick() {
        sw_week.setOnCheckedChangeListener { button, b ->
            sw_month.isChecked = false
            sw_week.isChecked = b
        }

        sw_month.setOnCheckedChangeListener { button, b ->
            sw_week.isChecked = false
            sw_month.isChecked = b
        }

        sw_one_times.setOnCheckedChangeListener { button, b ->
            sw_two_times.isChecked = false
            sw_three_times.isChecked = false
            sw_one_times.isChecked = b
        }

        sw_two_times.setOnCheckedChangeListener { button, b ->
            sw_one_times.isChecked = false
            sw_three_times.isChecked = false
            sw_two_times.isChecked = b
        }
        sw_three_times.setOnCheckedChangeListener { button, b ->
            sw_one_times.isChecked = false
            sw_two_times.isChecked = false
            sw_three_times.isChecked = b
        }
    }
}
