package juniar.porkat.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class PelangganModel(@SerializedName("id_pelanggan") val idPelanggan: Int,
                          @SerializedName("id_pengguna") val idPengguna: String,
                          @SerializedName("katasandi") val katasandi: String,
                          @SerializedName("no_telp") val noTelp: String,
                          @SerializedName("nama_lengkap") val namaLengkap: String,
                          @SerializedName("alamat") val alamat: String,
                          @SerializedName("status") val status: String)