package juniar.porkat.auth.register

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
data class RegisterPelangganRequest(@SerializedName("id_pengguna") var idPengguna: String = "",
                                    @SerializedName("katasandi") var katasandi: String = "",
                                    @SerializedName("no_telp") var noTelp: String = "",
                                    @SerializedName("nama_lengkap") var namaLengkap: String = "",
                                    @SerializedName("alamat") var alamat: String = "")