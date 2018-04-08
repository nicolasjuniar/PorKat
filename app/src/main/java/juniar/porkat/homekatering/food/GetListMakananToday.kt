package juniar.porkat.homekatering.food

import com.google.gson.annotations.SerializedName

data class GetListMakananToday(@SerializedName("listmenu") val listMenu:MutableList<MenuByDateModel>)