package juniar.porkat.Utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.CursorLoader
import android.support.v7.app.AlertDialog
import android.text.Html
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import juniar.porkat.R
import juniar.porkat.auth.KateringModel
import juniar.porkat.auth.PelangganModel
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_KATERING
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_PELANGGAN
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.text.DateFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
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

fun getTimeStamp(): String {
    return SimpleDateFormat("yyyyMMdd_Hmmss", Locale.getDefault()).format(Date())
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
        address = addresses[0].getAddressLine(0)

    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
    }

    return address.substringBefore(",")
}

fun String.logDebug(TAG: String = "debug") {
    Log.d(TAG, this)
}

fun Any.encodeJson(): String {
    return Gson().toJson(this)
}

fun getProfilePelanggan(sharedPreferenceUtil: SharedPreferenceUtil): PelangganModel {
    return Gson().fromJson(sharedPreferenceUtil.getString(PROFILE_PELANGGAN), PelangganModel::class.java)
}

fun getProfileKatering(sharedPreferenceUtil: SharedPreferenceUtil): KateringModel {
    return Gson().fromJson(sharedPreferenceUtil.getString(PROFILE_KATERING), KateringModel::class.java)
}

fun changeDateFormat(input: String, oldPattern: String, newPattern: String): String {
    val oldFormat = DateTimeFormat.forPattern(oldPattern)
    val oldDateTime = oldFormat.parseDateTime(input.toLocaleEnglish(oldPattern))
    val newFormat = DateTimeFormat.forPattern(newPattern)
    val newDateTime = DateTime.parse(newFormat.print(oldDateTime), newFormat)
    return newDateTime.toString(newPattern, Locale("id", "ID", "ID"))
}

fun String.toLocaleEnglish(pattern: String): String {
    val format = DateTimeFormat.forPattern(pattern)
    return format.parseDateTime(this).toString(pattern, Locale.ENGLISH)
}

fun getDateTimeAnotherFormat(input: String, oldPattern: String, newPattern: String): DateTime {
    val oldFormat = DateTimeFormat.forPattern(oldPattern)
    val oldDateTime = oldFormat.parseDateTime(input)
    val newFormat = DateTimeFormat.forPattern(newPattern)
    return DateTime.parse(newFormat.print(oldDateTime), newFormat)
}

fun String.toDateTime(pattern: String): DateTime {
    val format = DateTimeFormat.forPattern(pattern)
    return format.parseDateTime(this)
}

fun getMonth(month: Int): String {
    return DateFormatSymbols().months[month]
}

fun Context.checkRequestPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Activity.makeRequest(permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
}

fun String.convertToIDR(): String {
    val indonesia = Locale("id", "ID")
    val indoFormat = NumberFormat.getCurrencyInstance(indonesia)
    return indoFormat.format(BigDecimal(this))
}

fun TextView.setErrorColor(isValid: Boolean, context: Context) {
    this.setTextColor(context.getColorCompat(if (isValid) R.color.hint_color else R.color.md_red_500))
}

fun TextInputLayout.setErrorText(isValid: Boolean, errorMessage: String) {
    with(this) {
        isErrorEnabled = !isValid
        error = if (isValid) null else errorMessage
    }
}

fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false)
    bm.recycle()
    return resizedBitmap
}

fun ImageView.encodeBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    (this.drawable as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun getCapturedPhotoBitmap(path: String): Bitmap {
    val options = BitmapFactory.Options()
    val requiredSize = 450
    var scale = 1
    while (options.outWidth / scale / 2 >= requiredSize && options.outHeight / scale / 2 >= requiredSize)
        scale *= 2
    options.inSampleSize = scale
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(path, options)
}

fun Activity.getStoragePhotoBitmap(uri: Uri?): Bitmap {
    val projection = arrayOf(MediaStore.MediaColumns.DATA)
    val cursorLoader = CursorLoader(this, uri, projection,
            null, null, null)
    val cursor = cursorLoader.loadInBackground()
    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
    cursor.moveToFirst()
    val selectedImagePath = cursor.getString(columnIndex)
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(selectedImagePath, options)
    val requiredSize = 450
    var scale = 1
    while (options.outWidth / scale / 2 >= requiredSize && options.outHeight / scale / 2 >= requiredSize)
        scale *= 2
    options.inSampleSize = scale
    options.inJustDecodeBounds = false
    val bitmap = BitmapFactory.decodeFile(selectedImagePath, options)

    val display = this.windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    val width = size.x
    return if (bitmap.width < width) {
        val resizedHeight = bitmap.height + (width - bitmap.width)
        getResizedBitmap(bitmap, width, resizedHeight)
    } else {
        bitmap
    }
}

fun Activity.getFileFromUri(uri: Uri): File {
    val projection = arrayOf(MediaStore.MediaColumns.DATA)
    val cursorLoader = CursorLoader(this, uri, projection,
            null, null, null)
    val cursor = cursorLoader.loadInBackground()
    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
    cursor.moveToFirst()
    var selectedImagePath = cursor.getString(columnIndex)

    return File(selectedImagePath)
}

fun getTimeNow(): String {
    val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    val dateTime = DateTime.parse(DateTime().toString(), DateTimeFormat.forPattern(pattern))
    return dateTime.toString("HH:mm")
}

fun getDifferenceTime(timeSend: String): Long {
    val pattern = "HH:mm"
    val format = DateTimeFormat.forPattern(pattern)
    val dtNow = format.parseDateTime(getTimeNow())
    val dtSend = format.parseDateTime(timeSend)
    return Duration(dtNow.millis, dtSend.millis).standardHours
}

fun decodePoly(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)
        poly.add(p)
    }

    return poly
}

fun Context.getMarkerFromDrawable(drawableResource:Int):BitmapDescriptor{
    var drawable = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
        this.getDrawable(drawableResource)
    }else{
        this.resources.getDrawable(drawableResource)
    }
    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    canvas.setBitmap(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

val sdkVersion = Build.VERSION.SDK_INT