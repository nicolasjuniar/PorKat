package juniar.porkat.login

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class PelangganModel(@SerializedName("id_pelanggan") val id_pelanggan: Int,
                          @SerializedName("id_pengguna") val id_pengguna: String,
                          @SerializedName("katasandi") val katasandi: String,
                          @SerializedName("no_telp") val no_telp: String,
                          @SerializedName("nama_lengkap") val nama_lengkap: String,
                          @SerializedName("alamat") val alamat: String,
                          @SerializedName("status") val status: String)