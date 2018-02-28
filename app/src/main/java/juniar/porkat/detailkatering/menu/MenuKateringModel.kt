package juniar.porkat.detailkatering.menu

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 26/02/2018.
 */
data class MenuKateringModel(@SerializedName("id_menu") val idMenu: Int,
                             @SerializedName("nama_menu") val namaMenu: String,
                             @SerializedName("foto") val foto: String,
                             @SerializedName("harga") val harga: Int,
                             @SerializedName("id_katering") val idKatering: Int)