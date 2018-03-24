package juniar.porkat.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Jarvis on 19/03/2018.
 */
data class TransactionResponse(@SerializedName("message") val message:String)