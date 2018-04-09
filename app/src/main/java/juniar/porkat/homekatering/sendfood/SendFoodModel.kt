package juniar.porkat.homekatering.sendfood

import com.google.gson.annotations.SerializedName

data class SendFoodModel(@SerializedName("id_pesan") val idPesan:Int,
                         @SerializedName("id_detailpesan") val idDetailPesan:Int,
                         @SerializedName("nama_lengkap") val namaLengkap:String,
                         @SerializedName("catatan") val catatan:String,
                         @SerializedName("alamat") val alamat:String,
                         @SerializedName("longitude") val longitude:Double,
                         @SerializedName("latitude") val latitude:Double,
                         @SerializedName("waktu_pengantaran") val waktuPengantaran:String,
                         @SerializedName("nama_menu") val namaMenu:String,
                         @SerializedName("foto") val foto:String,
                         @SerializedName("status") val status:String)