package juniar.porkat.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class PelangganModel(@SerializedName("id_pelanggan") var idPelanggan: Int,
                          @SerializedName("id_pengguna") var idPengguna: String,
                          @SerializedName("katasandi") var katasandi: String,
                          @SerializedName("no_telp") var noTelp: String,
                          @SerializedName("nama_lengkap") var namaLengkap: String,
                          @SerializedName("alamat") var alamat: String,
                          @SerializedName("status") var status: String)