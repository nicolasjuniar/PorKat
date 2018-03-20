package juniar.porkat.detailkatering.review

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 01/03/2018.
 */
data class InsertReviewRequest(@SerializedName("ulasan") val ulasan:String,
                               @SerializedName("rating") val rating:Float,
                               @SerializedName("idPelanggan") val idPelanggan:Int,
                               @SerializedName("id_katering") val idKatering:Int)