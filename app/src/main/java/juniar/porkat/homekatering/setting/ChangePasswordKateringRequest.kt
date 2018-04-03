package juniar.porkat.homekatering.setting

import com.google.gson.annotations.SerializedName

data class ChangePasswordKateringRequest(@SerializedName("id_katering") val idKatering: Int,
                                         @SerializedName("katasandi") val katasandi: String)