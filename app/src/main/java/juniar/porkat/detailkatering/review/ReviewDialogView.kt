package juniar.porkat.detailkatering.review

/**
 * Created by Nicolas Juniar on 01/03/2018.
 */
interface ReviewDialogView{
    fun onInsertReview(error: Boolean, insertReviewResponse: InsertReviewResponse?, t: Throwable?)
    fun onUpdateReview(error: Boolean, updateReviewResponse: UpdateReviewResponse?, t: Throwable?)
}