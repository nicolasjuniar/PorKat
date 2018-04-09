package juniar.porkat.homekatering.sendfood

import com.google.gson.annotations.SerializedName

data class GetListSendFoodResponse(@SerializedName("list_transaksi") val listTransaksi: MutableList<SendFoodModel>)