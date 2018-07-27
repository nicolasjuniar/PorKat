package juniar.porkat

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class PorkatApp : Application() {
    companion object {
        val BASE_URL = "http://192.168.43.168/porkat_web/"
//        val BASE_URL = "http://192.168.1.120/porkat_web/" //Binar Punya
//        const val BASE_URL = "http://192.168.23.225/porkat_web/" //Inet Sinergi
//        val BASE_URL = "http://192.168.100.12/porkat_web/" //Irfan Kontrakan
//        val BASE_URL = "http://10.201.110.198/porkat_web/" //Mekdi
    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}