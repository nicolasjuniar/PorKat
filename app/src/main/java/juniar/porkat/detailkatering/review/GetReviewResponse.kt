package juniar.porkat.detailkatering.review

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
data class GetReviewResponse(@SerializedName("listulasan") val listUlasan: MutableList<ReviewModel>,
                             @SerializedName("ulasanpelanggan") val ulasanPelanggan: ReviewModel)