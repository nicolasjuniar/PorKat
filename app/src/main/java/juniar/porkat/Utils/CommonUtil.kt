package juniar.porkat.Utils

import android.content.Context
import android.content.DialogInterface
import android.location.Geocoder
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import juniar.porkat.R
import juniar.porkat.auth.KateringModel
import juniar.porkat.auth.PelangganModel
import juniar.porkat.common.Constant.CommonStrings.Companion.PELANGGAN
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_KATERING
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_PELANGGAN
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */

@Suppress("DEPRECATION")
fun String.toHtmlText() = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(this)

fun Context.showSnackBar(view: View, text: String, duration: Int = Snackbar.LENGTH_INDEFINITE, textColor: Int = ContextCompat.getColor(this, R.color.Red), dismissEvent: () -> Unit = {}) {
    val snackbar = Snackbar.make(view, text, duration)
    snackbar.setAction(android.R.string.ok, { snackbar.dismiss() })
    snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.Gray))
    snackbar.duration = 4000
    snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.White))
    snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text).setTextColor(textColor)
    snackbar.show()
    snackbar.addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            dismissEvent.invoke()
        }
    })
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun isPhoneValid(phone: String): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}

fun alphabetOnly(text: String): Boolean {
    val expression = "^[\\p{L} .'-]+$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(text)
    return matcher.matches()
}

fun Context.showShortToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.getColorCompat(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

fun changeDateFormat(input: String): String {
    val oldFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")
    val oldDateTime = oldFormat.parseDateTime(input)
    val newFormat = DateTimeFormat.forPattern("d MMMM yyyy")
    val newDateTime = DateTime.parse(newFormat.print(oldDateTime), newFormat)
    return newDateTime.toString("d MMMM yyyy", Locale("in_ID"))
}

fun Context.buildAlertDialog(title: String, message: String = "", yesButton: String = "", noButton: String = "", positiveAction: (DialogInterface) -> Unit = {}, negativeAction: (DialogInterface) -> Unit = {}): AlertDialog {
    val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)

    if (yesButton.isNotEmpty()) {
        builder.setPositiveButton(yesButton, { dialog, _ -> positiveAction.invoke(dialog) })
    }

    if (noButton.isNotEmpty()) {
        builder.setNegativeButton(noButton, { dialog, _ -> negativeAction.invoke(dialog) })
    }

    return builder.create()
}

fun Button.setAvailable(enable: Boolean, context: Context) {
    with(this) {
        isEnabled = enable
        setBackgroundResource(if (enable) R.drawable.rounded_button_enabled else R.drawable.rounded_button_disabled)
        setTextColor(if (enable) context.getColorCompat(R.color.White) else context.getColorCompat(R.color.light_tosca))
    }
}

val DONT_TOUCH = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE

fun EditText.textToString(): String {
    return this.text.toString()
}

fun Context.getAddress(lat: Double, lng: Double): String {
    val geocoder = Geocoder(this, Locale.getDefault())
    var address = ""
    try {
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        val obj = addresses[0]
        address = addresses[0].thoroughfare + " " + addresses[0].subThoroughfare + ", " + addresses[0].subLocality

    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
    }

    return address
}

fun String.logDebug() {
    Log.d("debug", this)
}

fun Any.encodeJson(): String {
    return Gson().toJson(this)
}

fun getProfilePelanggan(sharedPreferenceUtil: SharedPreferenceUtil):PelangganModel{
    return Gson().fromJson(sharedPreferenceUtil.getString(PROFILE_PELANGGAN),PelangganModel::class.java)
}

fun getProfileKatering(sharedPreferenceUtil: SharedPreferenceUtil):KateringModel{
    return Gson().fromJson(sharedPreferenceUtil.getString(PROFILE_KATERING),KateringModel::class.java)
}