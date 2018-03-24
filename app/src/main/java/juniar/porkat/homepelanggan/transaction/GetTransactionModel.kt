package juniar.porkat.homepelanggan.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 22/03/2018.
 */
data class GetTransactionModel(@SerializedName("id_pesan") val idPesan:Int,
                               @SerializedName("nama_katering") val namaKatering:String,
                               @SerializedName("tgl_mulai") val tglMulai:String,
                               @SerializedName("tgl_selesai") val tglSelsai:String,
                               @SerializedName("alamat") val alamat:String,
                               @SerializedName("catatan") val catatan:String,
                               @SerializedName("nota") val nota:String,
                               @SerializedName("total") val total:Int,
                               @SerializedName("status") val status:String)