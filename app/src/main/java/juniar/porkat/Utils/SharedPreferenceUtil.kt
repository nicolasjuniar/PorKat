package juniar.porkat.Utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class SharedPreferenceUtil(context: Context) {
    companion object NetworkKeys{
        val NETWORK_HEADER_TOKEN = "header_token"
    }

    private val sharedPreferenceUtil: SharedPreferences = context.getSharedPreferences("porkat_preferences", Context.MODE_PRIVATE)

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferenceUtil.edit().putBoolean(key, value).apply()
    }

    fun setString(key: String, value: String) {
        sharedPreferenceUtil.edit().putString(key, value).apply()
    }

    fun setInt(key: String, value: Int) {
        sharedPreferenceUtil.edit().putInt(key, value).apply()
    }

    fun setLong(key: String, value: Long) {
        sharedPreferenceUtil.edit().putLong(key, value).apply()
    }

    fun getBoolean(key: String) = sharedPreferenceUtil.getBoolean(key, false)

    fun getString(key: String) = sharedPreferenceUtil.getString(key, "")

    fun getInt(key: String) = sharedPreferenceUtil.getInt(key, 0)

    fun getLong(key: String) = sharedPreferenceUtil.getLong(key, 0)
}