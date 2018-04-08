package juniar.porkat.homekatering

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.KateringModel
import juniar.porkat.auth.register.SetKateringPhotoFragment
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_KATERING
import kotlinx.android.synthetic.main.activity_edit_photo_katering.*
import java.io.File
import java.io.IOException

class EditPhotoKateringActivity:BaseActivity<EditKateringPresenter>(),EditPhotoKateringView{

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    private var imageFilePath = ""
    private var photoName = ""
    lateinit var katering:KateringModel

    override fun onSetupLayout() {
        setContentView(R.layout.activity_edit_photo_katering)
        setupToolbarTitle(toolbar_layout as Toolbar,getString(R.string.edit_photo_katering_text))
    }

    override fun onViewReady() {
        sharedPreferenceUtil= SharedPreferenceUtil(this@EditPhotoKateringActivity)
        presenter=EditKateringPresenter(this)
        katering= getProfileKatering(sharedPreferenceUtil)
        if(katering.foto.isNotEmpty()){
            Picasso.with(this@EditPhotoKateringActivity)
                    .load("${PorkatApp.BASE_URL}/foto/katering/${katering.foto}")
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(500, 500)
                    .into(iv_photo_katering)

            btn_upload.setAvailable(true,this@EditPhotoKateringActivity)
        }

        iv_take_photo.setOnClickListener {
            selectImage()
        }

        btn_upload.setOnClickListener {
            photoName=if(katering.foto.isNotEmpty()) katering.foto else photoName
            setLoading(true)
            presenter?.updatePhotoKatering(UpdatePhotoKateringRequest(katering.idKatering,photoName,iv_photo_katering.encodeBase64()))
        }
    }

    override fun setLoading(loading: Boolean) {
        if(loading){
            pb_loading.show()
        }else{
            pb_loading.hide()
        }
    }

    override fun onSuccessUploadPhoto(error: Boolean, message: String?, t: Throwable?) {
        setLoading(false)
        if(!error){
            message?.let {
                showShortToast(it)
            }
            katering.foto=photoName
            sharedPreferenceUtil.setString(PROFILE_KATERING,katering.encodeJson())
            setResult(Activity.RESULT_OK)
            finish()
        }else{
            t?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SetKateringPhotoFragment.REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    with(iv_photo_katering) {
                        setImageBitmap(getCapturedPhotoBitmap(imageFilePath))
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    btn_upload.setAvailable(true,this@EditPhotoKateringActivity)
                }
            }
            Constant.CommonInt.READ_EXTERNAL_STORAGE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    with(iv_photo_katering) {
                        setImageBitmap(getStoragePhotoBitmap(data?.data))
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    photoName = "IMG_${getTimeStamp()}.jpg"
                    btn_upload.setAvailable(true,this@EditPhotoKateringActivity)
                }
            }
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(getString(R.string.take_from_camera), getString(R.string.pick_from_gallery_text), getString(R.string.cancel_text))

        val builder = AlertDialog.Builder(this@EditPhotoKateringActivity)
        with(builder) {
            setTitle(getString(R.string.add_photo_text))
            setItems(items) { dialog, item ->
                when {
                    items[item] == getString(R.string.take_from_camera) -> {
                        if (sdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                            if (checkRequestPermission(android.Manifest.permission.CAMERA)) {
                                openCameraIntent()
                            } else {
                                makeRequest(Manifest.permission.CAMERA, Constant.CommonInt.CAMERA_INTENT_CODE)
                            }
                        } else {
                            makeRequest(Manifest.permission.CAMERA, Constant.CommonInt.CAMERA_INTENT_CODE)
                        }
                    }
                    items[item] == getString(R.string.pick_from_gallery_text) ->
                        if (sdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                            if (checkRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                takeImage()
                            } else {
                                makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, Constant.CommonInt.READ_EXTERNAL_STORAGE_CODE)
                            }
                        } else {
                            makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, Constant.CommonInt.READ_EXTERNAL_STORAGE_CODE)
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
        pictureIntent.resolveActivity(packageManager)?.let {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            photoFile?.let {
                val file = it
                val photoUri = FileProvider.getUriForFile(this@EditPhotoKateringActivity, "$packageName.provider", file)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(pictureIntent, SetKateringPhotoFragment.REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun createImageFile(): File {
        photoName = "IMG_${getTimeStamp()}.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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

}