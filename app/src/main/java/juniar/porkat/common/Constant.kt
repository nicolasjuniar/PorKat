package juniar.porkat.common

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
class Constant {

    interface CommonStrings {
        companion object {
            val EMPTY_STRING = ""
            val SESSION="session"
            val ROLE="role"
            val PELANGGAN="pelanggan"
            val KATERING="katering"
            val PROFILE_PELANGGAN="profile_pelanggan"
            val PROFILE_KATERING="profile_katering"
            val LONGITUDE="longitude"
            val LATITUDE="latitude"
            val DETAIL_TRANSAKSI="detail_transaksi"
        }
    }

    interface CommonInt{
        companion object {
            val ACCESS_FINE_LOCATION_CODE=101
            val READ_EXTERNAL_STORAGE_CODE=102
            val CAMERA_INTENT_CODE=103
        }
    }
}
