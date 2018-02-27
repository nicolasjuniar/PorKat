package juniar.porkat.detailkatering

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
data class GetReviewResponse(@SerializedName("listulasan") val listulasan: MutableList<ReviewModel>,
                             @SerializedName("ulasanpelanggan") val ulasanpelanggan: ReviewModel)