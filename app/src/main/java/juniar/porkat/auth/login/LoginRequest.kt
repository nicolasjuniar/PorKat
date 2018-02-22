package juniar.porkat.auth.login

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class LoginRequest(@SerializedName("id_pengguna") val idPengguna:String,
                        @SerializedName("katasandi") val katasandi:String)