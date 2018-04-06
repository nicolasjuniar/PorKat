package juniar.porkat.homekatering.menu

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
import com.jakewharton.rxbinding2.widget.RxTextView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.register.SetKateringPhotoFragment
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant
import juniar.porkat.detailkatering.menu.MenuKateringModel
import kotlinx.android.synthetic.main.activity_edit_menu.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditMenuActivity : BaseActivity<EditMenuPresenter>(), EditMenuView {

    private var imageFilePath = ""
    private var photoName = ""
    private var setPhoto = false
    lateinit var menuKatering: MenuKateringModel

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    companion object {
        const val DETAIL_MENU = "menu"
        const val ACTION = "action"
        const val EDIT = "edit"
        const val ADD = "add"
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_edit_menu)
        setupToolbarTitle(toolbar_layout as Toolbar, getString(R.string.edit_menu_text))
    }

    override fun onViewReady() {
        presenter = EditMenuPresenter(this)
        if (intent.getStringExtra(ACTION) == EDIT) {
            menuKatering = intent.extras.get(DETAIL_MENU) as MenuKateringModel
            et_menu_name.setText(menuKatering.namaMenu)
            et_price.setText(menuKatering.harga.toString())
            Picasso.with(this@EditMenuActivity)
                    .load("${PorkatApp.BASE_URL}/foto/menu/${menuKatering.foto}")
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(500, 500)
                    .placeholder(R.drawable.default_food)
                    .into(iv_menu)
            setPhoto = true
            btn_add.hide()
            btn_edit.show()
            btn_delete.show()
            btn_delete.setAvailable(true, this@EditMenuActivity)
            changeTitleToolbar(R.string.edit_menu_text)
        } else {
            btn_add.show()
            btn_edit.hide()
            btn_delete.hide()
            changeTitleToolbar(getString(R.string.add_menu_text))
        }

        sharedPreferenceUtil = SharedPreferenceUtil(this@EditMenuActivity)
        val katering = getProfileKatering(sharedPreferenceUtil)

        presenter?.setMenuValidation(Observable.combineLatest(
                RxTextView.textChanges(et_menu_name)
                        .map { it.isNotEmpty() },
                RxTextView.textChanges(et_price)
                        .map { it.isNotEmpty() },
                BiFunction { name: Boolean, price: Boolean ->
                    name && price && setPhoto
                }), {
            btn_add.setAvailable(it, this@EditMenuActivity)
            btn_edit.setAvailable(it, this@EditMenuActivity)
        })

        iv_take_photo.setOnClickListener {
            selectImage()
        }

        btn_add.setOnClickListener {
            setLoading(true)
            presenter?.insertMenu(InsertMenuRequest(et_menu_name.textToString(), photoName, et_price.textToString().toInt(), katering.idKatering, iv_menu.encodeBase64()))
        }

        btn_edit.setOnClickListener {
            buildAlertDialog(getString(R.string.dialog_update_menu_title), getString(R.string.dialog_update_menu_message, menuKatering.namaMenu), getString(R.string.yes_dialog), getString(R.string.no_dialog), {
                setLoading(true)
                presenter?.updateMenu(UpdateMenuRequest(menuKatering.idMenu, et_menu_name.textToString(),menuKatering.foto, et_price.textToString().toInt(), iv_menu.encodeBase64()))
            }).show()
        }

        btn_delete.setOnClickListener {
            buildAlertDialog(getString(R.string.dialog_delete_menu_title), getString(R.string.dialog_delete_menu_message, menuKatering.namaMenu), getString(R.string.yes_dialog), getString(R.string.no_dialog), {
                setLoading(true)
                presenter?.deleteMenu(DeleteMenuRequest(menuKatering.idMenu))
            }).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SetKateringPhotoFragment.REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    with(iv_menu) {
                        setImageBitmap(getCapturedPhotoBitmap(imageFilePath))
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    setPhoto = true
                    btn_add.setAvailable(et_menu_name.textToString().isNotEmpty() && et_price.textToString().isNotEmpty() && setPhoto, this@EditMenuActivity)
                }
            }
            Constant.CommonInt.READ_EXTERNAL_STORAGE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    with(iv_menu) {
                        setImageBitmap(getStoragePhotoBitmap(data?.data))
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    setPhoto = true
                    btn_add.setAvailable(et_menu_name.textToString().isNotEmpty() && et_price.textToString().isNotEmpty() && setPhoto, this@EditMenuActivity)
                }
            }
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(getString(R.string.take_from_camera), getString(R.string.pick_from_gallery_text), getString(R.string.cancel_text))

        val builder = AlertDialog.Builder(this@EditMenuActivity)
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
                val photoUri = FileProvider.getUriForFile(this@EditMenuActivity, "$packageName.provider", file)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(pictureIntent, SetKateringPhotoFragment.REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_Hmmss", Locale.getDefault()).format(Date())
        photoName = "IMG_$timeStamp.jpg"
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

    override fun onInsertMenu(error: Boolean, message: String?, t: Throwable?) {
        setLoading(false)
        if (!error) {
            message?.let {
                showShortToast(it)
            }
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            t?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }

    override fun onDeleteMenu(error: Boolean, message: String?, t: Throwable?) {
        setLoading(false)
        if (!error) {
            message?.let {
                showShortToast(it)
            }
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            t?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }

    override fun onUpdateMenu(error: Boolean, message: String?, t: Throwable?) {
        setLoading(false)
        if (!error) {
            message?.let {
                showShortToast(it)
            }
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            t?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            progressbar.show()
        } else {
            progressbar.hide()
        }
    }

}