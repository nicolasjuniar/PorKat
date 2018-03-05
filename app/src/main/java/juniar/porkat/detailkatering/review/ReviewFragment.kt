package juniar.porkat.detailkatering.review

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.PelangganModel
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.menu.MenuFragment.Companion.ID_KATERING
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.viewholder_review.view.*

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
class ReviewFragment : BaseFragment<ReviewPresenter>(), ReviewView {

    lateinit var listReview: MutableList<ReviewModel>
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    lateinit var pelanggan: PelangganModel
    lateinit var review: ReviewModel

    companion object {
        val ID_PELANGGAN="id_pelanggan"
        val REVIEW="Review"
    }

    private val reviewAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_review, listReview,
                { review, _, _ ->
                },
                { review, view ->
                    with(review) {
                        view.tv_name.text = this.namaLengkap
                        view.tv_date.text = changeDateFormat(this.waktuUlasan)
                        view.rb_review.rating = this.rating
                        view.tv_review.text = this.ulasan
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_review, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = ReviewPresenter(this)
        sharedPreferenceUtil = SharedPreferenceUtil(activity)
        pelanggan = getProfilePelanggan(sharedPreferenceUtil)
        val idKatering = arguments.getInt(ID_KATERING)
        presenter?.getListMenuKatering(idKatering, pelanggan.id_pelanggan)

        swipe_layout.setOnRefreshListener({
            presenter?.getListMenuKatering(idKatering, pelanggan.id_pelanggan)
        })

        rv_review.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                swipe_layout.isEnabled = topRowVerticalPosition >= 0
            }
        })

        cv_add_review.setOnClickListener {
            var args=Bundle()
            arguments.putInt(ID_KATERING,idKatering)
            arguments.putInt(ID_PELANGGAN,pelanggan.id_pelanggan)
            val reviewDialog=ReviewDialog()
            reviewDialog.arguments=args
            reviewDialog.show(activity.fragmentManager, REVIEW)
        }

        ic_delete.setOnClickListener {
            activity.buildAlertDialog(getString(R.string.delete_review_dialog_title), yesButton = getString(R.string.yes_dialog), noButton = getString(R.string.no_dialog), positiveAction = {
                setHorizontalProgress(true)
                presenter?.deleteReview(review.idUlasan)
            }).show()
        }
    }

    fun setMyReview(review: ReviewModel) {
        rb_review.rating = review.rating
        tv_review.text = review.ulasan
        tv_review.show()
        add_review.hide()
        cv_add_review.isClickable = false
        ic_delete.show()
        ic_edit.show()
        my_review.show()
    }

    fun deleteMyReview() {
        tv_review.hide()
        add_review.show()
        cv_add_review.isClickable = true
        rb_review.rating = 0.0f
        ic_delete.hide()
        ic_edit.hide()
        my_review.hide()
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            progressbar.show()
        } else {
            progressbar.hide()
        }
    }

    override fun setHorizontalProgress(loading: Boolean) {
        if (loading) {
            activity.window.setFlags(DONT_TOUCH, DONT_TOUCH)
            pb_loading.show()
        } else {
            activity.window.clearFlags(DONT_TOUCH)
            pb_loading.hide()
        }
    }

    override fun onGetReview(error: Boolean, review: GetReviewResponse?, t: Throwable?) {
        setLoading(false)
        swipe_layout.isRefreshing = false
        if (!error) {
            review?.ulasanpelanggan?.let {
                setMyReview(it)
                this.review = it
            }
            with(rv_review) {
                listReview = review!!.listulasan
                adapter = reviewAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        } else {
            context.showShortToast(t?.localizedMessage!!)
        }
    }

    override fun onDeleteReview(error: Boolean, message: String?, t: Throwable?) {
        setHorizontalProgress(false)
        if (!error) {
            deleteMyReview()
            activity.showShortToast(message!!)
        } else {
            activity.showShortToast(t?.localizedMessage!!)
        }
    }
}