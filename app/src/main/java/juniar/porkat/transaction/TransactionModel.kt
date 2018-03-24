package juniar.porkat.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 19/03/2018.
 */
data class TransactionModel(@SerializedName("id_pesan") var idPesan: Int? = null,
                            @SerializedName("id_pelanggan") var idPelanggan: Int = -1,
                            @SerializedName("id_katering") var idKatering: Int = -1,
                            @SerializedName("tgl_mulai") var tglMulai: String = "",
                            @SerializedName("tgl_selesai") var tglSelesai: String = "",
                            @SerializedName("catatan") var catatan: String = "",
                            @SerializedName("nota") var nota: String = "",
                            @SerializedName("alamat") var alamat: String = "",
                            @SerializedName("longitude") var longitude: Double = -1.0,
                            @SerializedName("latitude") var latitude: Double = -1.0,
                            @SerializedName("total") var total: Int = -1)