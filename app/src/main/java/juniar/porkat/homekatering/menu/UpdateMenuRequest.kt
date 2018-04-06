package juniar.porkat.homekatering.menu

import com.google.gson.annotations.SerializedName

data class UpdateMenuRequest(@SerializedName("id_menu") val idMenu:Int,
                             @SerializedName("nama_menu") val namaMenu:String,
                             @SerializedName("foto") val foto:String,
                             @SerializedName("harga") val harga:Int,
                             @SerializedName("encoded_image") val encodedImage:String)