package juniar.porkat.detailkatering

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
interface ReviewView{
    fun setLoading(loading: Boolean)
    fun onGetListReview(error: Boolean, list: MutableList<ReviewModel>?, t: Throwable?)
}