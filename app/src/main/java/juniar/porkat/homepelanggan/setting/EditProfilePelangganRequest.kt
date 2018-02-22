package juniar.porkat.homepelanggan.setting

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
data class EditProfilePelangganRequest(@SerializedName("id_pelanggan") val idPelanggan:Int,
                                       @SerializedName("no_telp") val noTelp:String,
                                       @SerializedName("nama_lengkap") val namaLengkap:String,
                                       @SerializedName("alamat") val alamat:String)