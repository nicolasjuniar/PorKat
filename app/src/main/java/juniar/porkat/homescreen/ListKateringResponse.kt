package juniar.porkat.homescreen

import com.google.gson.annotations.SerializedName

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
data class ListKateringResponse(@SerializedName("listkatering") var listkatering: MutableList<GetKateringModel>)