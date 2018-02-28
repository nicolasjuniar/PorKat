package juniar.porkat.detailkatering.review

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.jakewharton.rxbinding2.widget.RxTextView
import juniar.porkat.R
import juniar.porkat.Utils.setAvailable
import juniar.porkat.Utils.textToString
import kotlinx.android.synthetic.main.dialog_review.*

/**
 * Created by Nicolas Juniar on 28/02/2018.
 */
class ReviewDialog : DialogFragment(), ReviewDialogView {

    lateinit var presenter: ReviewPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.dialog_review, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = ReviewPresenter(view2 = this)
        dialog.let {
            it.window.requestFeature(Window.FEATURE_NO_TITLE)
            it.setCanceledOnTouchOutside(false)
        }

        rb_review.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            if (rating < 1.0f)
                ratingBar.rating = 1.0f
        }

        RxTextView.textChanges(et_review)
                .map { it.isNotEmpty() }
                .subscribe { btn_send.setAvailable(it, activity) }

        btn_send.setOnClickListener {
            presenter.insertReview(InsertReviewRequest(et_review.textToString(),rb_review.rating,1,1))
        }
    }

    override fun onInsertReview(error: Boolean, insertReviewResponse: InsertReviewResponse?, t: Throwable?) {
    }

    override fun onUpdateReview(error: Boolean, updateReviewResponse: UpdateReviewResponse?, t: Throwable?) {
    }
}