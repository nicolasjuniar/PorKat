package juniar.porkat.homekatering

import com.google.gson.annotations.SerializedName

data class UpdatePhotoKateringRequest(@SerializedName("id_katering") val idKatering:Int,
                                      @SerializedName("foto") val foto:String,
                                      @SerializedName("encoded_image") val encodedImage:String)