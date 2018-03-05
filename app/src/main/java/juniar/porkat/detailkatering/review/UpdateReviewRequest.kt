package juniar.porkat.detailkatering.review

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 01/03/2018.
 */
data class UpdateReviewRequest(@SerializedName("ulasan") val ulasan: String,
                               @SerializedName("rating") val rating: Float,
                               @SerializedName("message") val message: String)