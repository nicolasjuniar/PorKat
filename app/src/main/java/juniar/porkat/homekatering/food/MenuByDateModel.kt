package juniar.porkat.homekatering.food

import com.google.gson.annotations.SerializedName

data class MenuByDateModel(@SerializedName("nama_menu") val namaMenu:String,
                           @SerializedName("jumlah") val jumlah:String,
                           @SerializedName("foto") val foto:String)