package juniar.porkat.login

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class LoginResponse(@SerializedName("success") val success: Boolean,
                         @SerializedName("role") val role: String,
                         @SerializedName("message") val message: String,
                         @SerializedName("datapelanggan") val datapelanggan: PelangganModel,
                         @SerializedName("datakatering") val datakatering: KateringModel)