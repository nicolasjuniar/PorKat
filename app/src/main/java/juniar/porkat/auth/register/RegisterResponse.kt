package juniar.porkat.auth.register

import com.google.gson.annotations.SerializedName
import juniar.porkat.auth.KateringModel
import juniar.porkat.auth.PelangganModel

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
data class RegisterResponse(@SerializedName("success") val success:Boolean,
                            @SerializedName("message") val message:String,
                            @SerializedName("datapelanggan") val dataPelanggan:PelangganModel,
                            @SerializedName("datakatering") val dataKatering:KateringModel)