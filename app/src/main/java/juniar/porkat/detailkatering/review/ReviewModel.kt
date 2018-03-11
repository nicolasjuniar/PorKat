package juniar.porkat.detailkatering.review

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
data class ReviewModel(@SerializedName("id_ulasan") var idUlasan: Int=-1,
                       @SerializedName("ulasan") var ulasan: String="",
                       @SerializedName("rating") var rating: Float=0.0f,
                       @SerializedName("waktu_ulasan") var waktuUlasan: String="",
                       @SerializedName("id_pengguna") var idPengguna: String="",
                       @SerializedName("nama_lengkap") var namaLengkap: String="")