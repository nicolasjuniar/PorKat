package juniar.porkat.homekatering.transaction

import com.google.gson.annotations.SerializedName

data class KateringTransactionModel(@SerializedName("id_pesan") val idPesan: Int,
                                    @SerializedName("nama_lengkap") val namaLengkap: String,
                                    @SerializedName("tgl_mulai") val tglMulai: String,
                                    @SerializedName("tgl_selesai") val tglSelesai: String,
                                    @SerializedName("alamat") val alamat: String,
                                    @SerializedName("catatan") val catatan: String,
                                    @SerializedName("nota") val nota: String,
                                    @SerializedName("total") val total: Int,
                                    @SerializedName("status") val status: String)