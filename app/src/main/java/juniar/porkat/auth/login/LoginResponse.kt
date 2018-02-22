package juniar.porkat.auth.login

import com.google.gson.annotations.SerializedName
import juniar.porkat.auth.KateringModel
import juniar.porkat.auth.PelangganModel

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class LoginResponse(@SerializedName("success") val success: Boolean,
                         @SerializedName("role") val role: String,
                         @SerializedName("message") val message: String,
                         @SerializedName("datapelanggan") val  datapelanggan: PelangganModel,
                         @SerializedName("datakatering") val datakatering: KateringModel)