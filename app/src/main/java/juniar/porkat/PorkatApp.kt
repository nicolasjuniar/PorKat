package juniar.porkat

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class PorkatApp : Application() {
    companion object {
        val BASE_URL = "http://192.168.43.168/porkat_web/"
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}