package juniar.porkat.homekatering.menu

import com.google.gson.annotations.SerializedName

data class InsertMenuRequest(@SerializedName("nama_menu") val namaMenu: String,
                             @SerializedName("foto") val foto: String,
                             @SerializedName("harga") val harga: Int,
                             @SerializedName("id_katering") val idKatering: Int,
                             @SerializedName("encoded_image") val encodedImage: String)