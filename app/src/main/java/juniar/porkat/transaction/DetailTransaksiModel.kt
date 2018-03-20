package juniar.porkat.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 19/03/2018.
 */
data class DetailTransaksiModel(@SerializedName("id_detailpesan") var idDetailPesan: Int? = null,
                                @SerializedName("id_pesan") var idPesan: Int? = null,
                                @SerializedName("waktu_pengantaran") var waktuPengantaran: String = "",
                                @SerializedName("id_menu") var idMenu: Int = -1)