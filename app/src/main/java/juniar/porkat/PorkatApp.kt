package juniar.porkat

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class PorkatApp : Application() {
    companion object {
//        val BASE_URL = "http://192.168.43.168/porkat_web/"
        val BASE_URL = "http://192.168.1.15/porkat_web/" //Binar Punya
//        const val BASE_URL = "http://192.168.23.225/porkat_web/" //Inet Sinergi
//        const val BASE_URL = "http://192.168.100.15/porkat_web/" //Irfan Kontrakan
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}