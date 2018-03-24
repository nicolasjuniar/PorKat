package juniar.porkat.homepelanggan.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 22/03/2018.
 */
data class GetTransactionResponse(@SerializedName("listtransaksi") val listtransaksi:MutableList<GetTransactionModel>)