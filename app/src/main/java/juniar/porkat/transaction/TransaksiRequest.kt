package juniar.porkat.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 19/03/2018.
 */
data class TransaksiRequest(@SerializedName("pesan") var transaksiModel: TransaksiModel=TransaksiModel(),
                            @SerializedName("detailpesan") var detailPesan: MutableList<DetailTransaksiModel>)