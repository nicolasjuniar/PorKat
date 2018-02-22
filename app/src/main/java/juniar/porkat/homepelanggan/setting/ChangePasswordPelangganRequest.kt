package juniar.porkat.homepelanggan.setting

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
data class ChangePasswordPelangganRequest(@SerializedName("id_pelanggan") val idPelanggan:Int,
                                          @SerializedName("katasandi") val katasandi:String)