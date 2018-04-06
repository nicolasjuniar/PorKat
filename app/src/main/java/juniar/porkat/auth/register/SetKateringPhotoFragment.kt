package juniar.porkat.auth.register

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant.CommonInt.Companion.CAMERA_INTENT_CODE
import juniar.porkat.common.Constant.CommonInt.Companion.READ_EXTERNAL_STORAGE_CODE
import kotlinx.android.synthetic.main.fragment_set_photo.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jarvis on 26/03/2018.
 */
class SetKateringPhotoFragment : BaseFragment<Any>() {

    private var imageFilePath = ""
    lateinit var callback: RegisterKateringView
    private var photoName = ""

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as RegisterKateringView
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 100
        fun newInstance(): SetKateringPhotoFragment {
            return SetKateringPhotoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_set_photo, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_take_photo.setOnClickListener {
            selectImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    with(iv_katering) {
                        setImageBitmap(getCapturedPhotoBitmap(imageFilePath))
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    callback.onGetPhotoKatering(photoName, iv_katering.encodeBase64())
                }
            }
            READ_EXTERNAL_STORAGE_CODE -> {
                if (resultCode == RESULT_OK)
                    activity?.let {
                        var thisActivity = it
                        with(iv_katering) {
                            setImageBitmap(thisActivity.getStoragePhotoBitmap(data?.data))
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                    }
                callback.onGetPhotoKatering(photoName, iv_katering.encodeBase64())
            }
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(getString(R.string.take_from_camera), getString(R.string.pick_from_gallery_text), getString(R.string.cancel_text))

        val builder = AlertDialog.Builder(activity)
        with(builder) {
            setTitle(getString(R.string.add_photo_text))
            setItems(items) { dialog, item ->
                when {
                    items[item] == getString(R.string.take_from_camera) -> {
                        activity?.let {
                            if (sdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                                if (it.checkRequestPermission(android.Manifest.permission.CAMERA)) {
                                    openCameraIntent()
                                } else {
                                    activity?.makeRequest(Manifest.permission.CAMERA, CAMERA_INTENT_CODE)
                                }
                            } else {
                                activity?.makeRequest(Manifest.permission.CAMERA, CAMERA_INTENT_CODE)
                            }
                        }
                    }
                    items[item] == getString(R.string.pick_from_gallery_text) -> activity?.let {
                        if (sdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                            if (it.checkRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                takeImage()
                            } else {
                                activity?.makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE)
                            }
                        } else {
                            activity?.makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE)
                        }
                    }
                    items[item] == getString(R.string.cancel_text) -> dialog.dismiss()
                }
            }
            show()
        }
    }

    private fun takeImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select File"), READ_EXTERNAL_STORAGE_CODE)
    }

    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pictureIntent.resolveActivity(context?.packageManager)?.let {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            photoFile?.let {
                val file = it
                context?.let {
                    val photoUri = FileProvider.getUriForFile(context, "${it.packageName}.provider", file)
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_Hmmss", Locale.getDefault()).format(Date())
        photoName = "IMG_$timeStamp.jpg"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(photoName, ".jpg", storageDir)
        imageFilePath = image.absolutePath
        return image
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeImage()
                }
            }
            CAMERA_INTENT_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraIntent()
                }
            }
        }
    }

}