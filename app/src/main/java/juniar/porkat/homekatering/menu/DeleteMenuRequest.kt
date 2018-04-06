package juniar.porkat.homekatering.menu

import com.google.gson.annotations.SerializedName

data class DeleteMenuRequest(@SerializedName("id_menu") val idMenu:Int,
                             @SerializedName("status") val status:Int=0)