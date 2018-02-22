package juniar.porkat.homepelanggan.setting

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
interface SettingView {
    fun setLoading(loading:Boolean)
    fun onUpdateProfile(error: Boolean, message: String?, t: Throwable?)
}