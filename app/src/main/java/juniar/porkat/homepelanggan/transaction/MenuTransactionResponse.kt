package juniar.porkat.homepelanggan.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 25/03/2018.
 */
data class MenuTransactionResponse(@SerializedName("list_menu") val listMenu:MutableList<MenuTransactionModel>)