package juniar.porkat.detailkatering.review

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
interface ReviewView {
    fun setLoading(loading: Boolean)
    fun setHorizontalProgress(loading: Boolean)
    fun onGetReview(error: Boolean, review: GetReviewResponse?, t: Throwable?)
    fun onDeleteReview(error: Boolean, message: String?, t: Throwable?)
    fun onInsertReview(review:InsertReviewResponse)
    fun onUpdateReview(review:UpdateReviewResponse)
}