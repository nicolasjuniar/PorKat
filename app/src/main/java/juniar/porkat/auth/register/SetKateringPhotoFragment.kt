package juniar.porkat.auth.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.CursorLoader
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import juniar.porkat.BuildConfig
import juniar.porkat.R
import juniar.porkat.Utils.checkRequestPermission
import juniar.porkat.Utils.makeRequest
import juniar.porkat.Utils.sdkVersion
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant.CommonInt.Companion.READ_EXTERNAL_STORAGE_CODE
import kotlinx.android.synthetic.main.fragment_set_photo.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jarvis on 26/03/2018.
 */
class SetKateringPhotoFragment : BaseFragment<Any>() {

    var timeStamp = ""
    lateinit var fileUri: Uri

    companion object {
        val MEDIA_TYPE_IMAGE = 1
        val COUNT_CAMERA = 100
        val PERMISSION_CALLBACK_CONSTANT = 100
        val REQUEST_PERMISSION_SETTING = 101
        val IMAGE_DIRECTORY_NAME = "MyQueue_CapturedPhoto"
        fun newInstance(): SetKateringPhotoFragment {
            return SetKateringPhotoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_set_photo, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add_photo.setOnClickListener {
//            selectImage()
        }
    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == COUNT_CAMERA) {
//
//                val bitmap: Bitmap
//                val options = BitmapFactory.Options()
//                options.inJustDecodeBounds = true
//                BitmapFactory.decodeFile(fileUri.getPath(), options)
//                //                    BitmapFactory.decodeFile(util.getPath(DetailTransaksiActivity.this, data.getData()), options);
//                val REQUIRED_SIZE = 450
//                var scale = 1
//                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                    scale *= 2
//                options.inSampleSize = scale
//                options.inJustDecodeBounds = false
//                bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options)
//                iv_katering.visibility = View.VISIBLE
//                iv_katering.setImageBitmap(bitmap)
//                iv_katering.scaleType = ImageView.ScaleType.FIT_XY
//                btn_add_photo.text = "Ganti Nota"
//
//            } else if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
//                val selectedImageUri = data.data
//                val projection = arrayOf(MediaStore.MediaColumns.DATA)
//                //                Cursor cursor = this.getContentResolver().query(selectedImageUri,  projection, null, null, null);
//                val cursorLoader = CursorLoader(activity, selectedImageUri, projection,
//                        null, null, null)
//                val cursor = cursorLoader.loadInBackground()
//                val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//                cursor.moveToFirst()
//
//                val selectedImagePath = cursor.getString(column_index)
//
//                val bm: Bitmap
//                val options = BitmapFactory.Options()
//                options.inJustDecodeBounds = true
//                BitmapFactory.decodeFile(selectedImagePath, options)
//                val REQUIRED_SIZE = 450
//                var scale = 1
//                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                    scale *= 2
//                options.inSampleSize = scale
//                options.inJustDecodeBounds = false
//                bm = BitmapFactory.decodeFile(selectedImagePath, options)
//
//                val display = activity.windowManager.defaultDisplay
//                val size = Point()
//                display.getSize(size)
//                val width = size.x
//
//                iv_katering.visibility = View.VISIBLE
//                if (bm.width < width) {
//                    val heighttoAdd = bm.height + (width - bm.width)
//                    val newResizedBitmap = getResizedBitmap(bm, width, heighttoAdd)
//                    iv_katering.setImageBitmap(newResizedBitmap)
//                } else {
//                    iv_katering.setImageBitmap(bm)
//                    iv_katering.scaleType = ImageView.ScaleType.FIT_CENTER
//                }
//                btn_add_photo.text = "Ganti Nota"
//            }
//        }
//    }
//
//    private fun selectImage() {
//        val items = arrayOf<CharSequence>("Ambil lewat kamera", "Pilih lewat penyimpanan", "Batal")
//
//        val builder = AlertDialog.Builder(activity)
//        builder.setTitle("Tambahkan Foto")
//        builder.setItems(items) { dialog, item ->
//            if (items[item] == "Ambil lewat kamera") {
//                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.CAMERA)) {
//                        //Show Information about why you need the permission
//                        val builder = AlertDialog.Builder(activity)
//                        builder.setTitle("Need Multiple Permissions")
//                        builder.setMessage("This app needs Camera and Location permissions.")
//                        builder.setPositiveButton("Grant") { dialog, _ ->
//                            dialog.cancel()
//                            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CAMERA), PERMISSION_CALLBACK_CONSTANT)
//                        }
//                        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
//                        builder.show()
//                    } else if (true) {
//                        //Previously Permission Request was cancelled with 'Dont Ask Again',
//                        // Redirect to Settings after showing Information about why you need the permission
//                        val builder = AlertDialog.Builder(activity)
//                        builder.setTitle("Need Multiple Permissions")
//                        builder.setMessage("This app needs Camera and Location permissions.")
//                        builder.setPositiveButton("Grant") { dialog, _ ->
//                            dialog.cancel()
//                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                            val uri = Uri.fromParts("package", activity.packageName, null)
//                            intent.data = uri
//                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
//                            activity.showShortToast("Go to Permissions to Grant  Camera and Location")
//                        }
//                        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
//                        builder.show()
//                    } else {
//                        //just request the permission
//                        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CAMERA), PERMISSION_CALLBACK_CONSTANT)
//                    }
//
//                } else {
//                    //You already have the permission, just go ahead.
//                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//                    startActivityForResult(intent, COUNT_CAMERA)
//                }
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//                startActivityForResult(intent, COUNT_CAMERA)
//            } else if (items[item] == "Pilih lewat penyimpanan") {
//                activity?.let {
//                    if (sdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
//                        if (it.checkRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                            takeImage()
//                        } else {
//                            activity?.let {
//                                it.makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE)
//                            }
//                        }
//                    }
//                }
//            } else if (items[item] == "Batal") {
//                dialog.dismiss()
//            }
//        }
//        builder.show()
//    }
//
//    fun takeImage(){
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intent.type = "image/*"
//        startActivityForResult(Intent.createChooser(intent, "Select File"), READ_EXTERNAL_STORAGE_CODE)
//    }
//
//    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
//        val width = bm.width
//        val height = bm.height
//        val scaleWidth = newWidth.toFloat() / width
//        val scaleHeight = newHeight.toFloat() / height
//        // CREATE A MATRIX FOR THE MANIPULATION
//        val matrix = Matrix()
//        // RESIZE THE BIT MAP
//        matrix.postScale(scaleWidth, scaleHeight)
//
//        // "RECREATE" THE NEW BITMAP
//        val resizedBitmap = Bitmap.createBitmap(
//                bm, 0, 0, width, height, matrix, false)
//        bm.recycle()
//        return resizedBitmap
//    }
//
//    fun getOutputMediaFileUri(type: Int): Uri {
//        return FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type))
////        return Uri.fromFile(getOutputMediaFile(type))
//    }
//
//    private fun getOutputMediaFile(type: Int): File? {
//        // External sdcard location
//        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME)
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                return null
//            }
//        }
//
//        // Create a media file name
//        val mediaFile: File
//        if (type == MEDIA_TYPE_IMAGE) {
//            generateTimeStamp()
//            mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + "_user3.jpg")
//        } else {
//            return null
//        }
//        MediaScannerConnection.scanFile(activity, arrayOf(Environment.getExternalStorageDirectory().toString()), null, MediaScannerConnection.OnScanCompletedListener { path, uri ->
//            Log.i("ExternalStorage", "Scanned $path:")
//            Log.i("ExternalStorage", "-> uri=$uri")
//        })
//        return mediaFile
//    }
//
//    private fun generateTimeStamp() { // KALO MW GENERATE WAKTU
//        timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            READ_EXTERNAL_STORAGE_CODE->{
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                }
//            }
//        }
//    }

}
