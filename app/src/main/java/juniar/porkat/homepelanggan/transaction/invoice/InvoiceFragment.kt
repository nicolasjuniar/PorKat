package juniar.porkat.homepelanggan.transaction.invoice

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
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
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.register.SetKateringPhotoFragment
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant
import juniar.porkat.common.Constant.CommonInt.Companion.READ_EXTERNAL_STORAGE_CODE
import juniar.porkat.homepelanggan.transaction.GetTransactionModel
import kotlinx.android.synthetic.main.fragment_invoice.*
import java.io.File
import java.io.IOException

class InvoiceFragment : BaseFragment<InvoicePresenter>(), InvoiceView {

    private var imageFilePath = ""
    private var photoName = ""
    lateinit var transactionModel: GetTransactionModel

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 100
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_invoice, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = InvoicePresenter(this)
        transactionModel = arguments.getParcelable(Constant.CommonStrings.DETAIL_TRANSAKSI) as GetTransactionModel
        iv_take_photo.setOnClickListener {
            selectImage()
        }

        if (transactionModel.nota.isNotEmpty()) {
            Picasso.with(activity)
                    .load("${PorkatApp.BASE_URL}/foto/nota/${transactionModel.nota}")
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(500, 500)
                    .placeholder(R.drawable.default_invoice)
                    .into(iv_invoice)

            btn_upload.setAvailable(true, activity)
        }

        btn_upload.setOnClickListener {
            activity?.let {
                it.buildAlertDialog(getString(R.string.dialog_upload_invoice_title), getString(R.string.dialog_update_invoice_message), getString(R.string.yes_dialog), getString(R.string.no_dialog)) {
                    setLoading(true)
                    photoName = if (transactionModel.nota.isNotEmpty()) transactionModel.nota else photoName
                    presenter?.updateInvoice(transactionModel.idPesan, photoName, iv_invoice.encodeBase64())
                }.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    with(iv_invoice) {
                        setImageBitmap(getCapturedPhotoBitmap(imageFilePath))
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    btn_upload.setAvailable(true, activity)
                }
            }
            READ_EXTERNAL_STORAGE_CODE -> {
                if (resultCode == Activity.RESULT_OK)
                    activity?.let {
                        var thisActivity = it
                        with(iv_invoice) {
                            setImageBitmap(thisActivity.getStoragePhotoBitmap(data?.data))
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        photoName = "IMG_${getTimeStamp()}.jpg"
                    }
                btn_upload.setAvailable(true, activity)
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
                                    activity?.makeRequest(Manifest.permission.CAMERA, Constant.CommonInt.CAMERA_INTENT_CODE)
                                }
                            } else {
                                activity?.makeRequest(Manifest.permission.CAMERA, Constant.CommonInt.CAMERA_INTENT_CODE)
                            }
                        }
                    }
                    items[item] == getString(R.string.pick_from_gallery_text) -> activity?.let {
                        if (sdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                            if (it.checkRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                takeImage()
                            } else {
                                activity?.makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, Constant.CommonInt.READ_EXTERNAL_STORAGE_CODE)
                            }
                        } else {
                            activity?.makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, Constant.CommonInt.READ_EXTERNAL_STORAGE_CODE)
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
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constant.CommonInt.READ_EXTERNAL_STORAGE_CODE)
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
                    startActivityForResult(pictureIntent, SetKateringPhotoFragment.REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(): File {
        photoName = "IMG_${getTimeStamp()}.jpg"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(photoName, ".jpg", storageDir)
        imageFilePath = image.absolutePath
        return image
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constant.CommonInt.READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeImage()
                }
            }
            Constant.CommonInt.CAMERA_INTENT_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraIntent()
                }
            }
        }
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            pb_loading.show()
        } else {
            pb_loading.hide()
        }
    }

    override fun onSuccessUpdateInvoice(error: Boolean, message: String?, t: Throwable?) {
        setLoading(false)
        if (!error) {
            message?.let {
                activity?.showShortToast(it)
            }
            activity?.let {
                it.setResult(RESULT_OK)
                it.finish()
            }
        } else {
            t?.let {
                activity?.showShortToast(it.localizedMessage)
            }
        }
    }
}