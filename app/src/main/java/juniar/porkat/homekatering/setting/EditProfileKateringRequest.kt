package juniar.porkat.homekatering.setting

import com.google.gson.annotations.SerializedName

class EditProfileKateringRequest(@SerializedName("id_katering") val idKatering:Int,
                                 @SerializedName("nama_katering") val namaKatering:String,
                                 @SerializedName("no_telp") val noTelp:String,
                                 @SerializedName("alamat") val alamat:String,
                                 @SerializedName("longitude") val longitude:Double,
                                 @SerializedName("latitude") val latitude:Double)