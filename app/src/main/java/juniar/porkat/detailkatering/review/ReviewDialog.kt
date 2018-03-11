package juniar.porkat.detailkatering.review

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.jakewharton.rxbinding2.widget.RxTextView
import juniar.porkat.R
import juniar.porkat.Utils.setAvailable
import juniar.porkat.Utils.textToString
import juniar.porkat.detailkatering.menu.MenuFragment.Companion.ID_KATERING
import juniar.porkat.detailkatering.review.ReviewFragment.Companion.ADD
import juniar.porkat.detailkatering.review.ReviewFragment.Companion.ID_PELANGGAN
import juniar.porkat.detailkatering.review.ReviewFragment.Companion.ID_ULASAN
import juniar.porkat.detailkatering.review.ReviewFragment.Companion.RATING
import juniar.porkat.detailkatering.review.ReviewFragment.Companion.TYPE
import juniar.porkat.detailkatering.review.ReviewFragment.Companion.ULASAN
import kotlinx.android.synthetic.main.dialog_review.*

/**
 * Created by Nicolas Juniar on 28/02/2018.
 */
class ReviewDialog : DialogFragment(), ReviewDialogView {

    lateinit var presenter: ReviewPresenter
    lateinit var callback: ReviewView
    var idKatering = -1
    var idPelanggan = -1
    var idUlasan = -1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.dialog_review, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = ReviewPresenter(view2 = this)
        dialog.let {
            it.window.requestFeature(Window.FEATURE_NO_TITLE)
            it.setCanceledOnTouchOutside(false)
        }

        callback = targetFragment as ReviewView
        if (arguments.getString(TYPE) == ADD) {
            idKatering = arguments.getInt(ID_KATERING)
            idPelanggan = arguments.getInt(ID_PELANGGAN)
        } else {
            idUlasan = arguments.getInt(ID_ULASAN)
            rb_review.rating = arguments.getFloat(RATING)
            et_review.setText(arguments.getString(ULASAN))
        }

        rb_review.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            if (rating < 1.0f)
                ratingBar.rating = 1.0f
        }

        RxTextView.textChanges(et_review)
                .map { it.isNotEmpty() }
                .subscribe { btn_send.setAvailable(it, activity) }

        ic_close.setOnClickListener {
            dismiss()
        }

        btn_send.setOnClickListener {
            if(arguments.getString(TYPE)==ADD){
                presenter.insertReview(InsertReviewRequest(et_review.textToString(), rb_review.rating, idPelanggan, idKatering))
            }else{
                presenter.updateReview(UpdateReviewRequest(idUlasan,et_review.textToString(),rb_review.rating))
            }
        }
    }

    override fun onInsertReview(error: Boolean, insertReviewResponse: InsertReviewResponse?, t: Throwable?) {
        callback.onInsertReview(insertReviewResponse!!)
        dismiss()
    }

    override fun onUpdateReview(error: Boolean, updateReviewResponse: UpdateReviewResponse?, t: Throwable?) {
        callback.onUpdateReview(updateReviewResponse!!)
        dismiss()
    }
}