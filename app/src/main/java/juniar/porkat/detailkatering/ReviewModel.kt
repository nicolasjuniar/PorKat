package juniar.porkat.detailkatering

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
data class ReviewModel(@SerializedName("id_ulasan") val idUlasan: Int,
                       @SerializedName("ulasan") val ulasan: String,
                       @SerializedName("rating") val rating: Float,
                       @SerializedName("waktu_ulasan") val waktuUlasan: String,
                       @SerializedName("id_pengguna") val idPengguna: String,
                       @SerializedName("nama_lengkap") val namaLengkap: String)