package juniar.porkat.homepelanggan.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 25/03/2018.
 */
data class MenuTransactionModel(@SerializedName("id_menu") val idMenu:String,
                                @SerializedName("waktu_pengantaran") val waktuPengantaran:String,
                                @SerializedName("nama_menu") val namaMenu:String,
                                @SerializedName("harga") val harga:Int,
                                @SerializedName("foto") val foto:String)