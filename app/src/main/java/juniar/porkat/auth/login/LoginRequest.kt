package juniar.porkat.login

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class LoginRequest(@SerializedName("id_pengguna") val id_pengguna:String,
                        @SerializedName("katasandi") val katasandi:String)