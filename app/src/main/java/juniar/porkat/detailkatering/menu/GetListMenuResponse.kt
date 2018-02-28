package juniar.porkat.detailkatering.menu

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 26/02/2018.
 */
data class GetListMenuResponse(@SerializedName("listmenu") val listMenu: MutableList<MenuKateringModel>)