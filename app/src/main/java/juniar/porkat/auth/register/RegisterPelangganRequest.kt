package juniar.porkat.auth.register

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
data class RegisterRequest(@SerializedName("idPengguna") val idPengguna: String,
                           @SerializedName("katasandi") val katasandi: String,
                           @SerializedName("no_telp") val noTelp: String,
                           @SerializedName("nama_lengkap") val namaLengkap: String,
                           @SerializedName("alamat") val alamat: String)