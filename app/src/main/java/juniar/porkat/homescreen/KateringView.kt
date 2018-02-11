package juniar.porkat.homescreen

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
interface KateringView{
    fun onGetListKatering(error: Boolean, response: ListKateringResponse?, t: Throwable?)
    fun setLoading(loading:Boolean)
}