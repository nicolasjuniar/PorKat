package juniar.porkat.homekatering.transaction

import com.google.gson.annotations.SerializedName

data class GetKateringTransactionResponse(@SerializedName("list_transaksi") val listTransaksi:MutableList<KateringTransactionModel>)