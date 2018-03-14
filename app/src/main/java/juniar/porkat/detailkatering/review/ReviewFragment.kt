package juniar.porkat.detailkatering.review

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.PelangganModel
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant.CommonStrings.Companion.SESSION
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.menu.MenuFragment.Companion.ID_KATERING
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.viewholder_review.view.*

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
class ReviewFragment : BaseFragment<ReviewPresenter>(), ReviewView {

    var listReview= mutableListOf<ReviewModel>()
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    lateinit var pelanggan: PelangganModel
    var review=ReviewModel()
    var idPelanggan = -1
    var idKatering=-1

    companion object {
        val ID_PELANGGAN = "id_pelanggan"
        val ID_ULASAN="id_ulasan"
        val REVIEW = "Review"
        val EDIT="edit"
        val ADD="add"
        val TYPE="type"
        val RATING="rating"
        val ULASAN="ulasan"
    }

    private val reviewAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_review, listReview,
                { review, _, _ ->
                },
                { review, view ->
                    with(review) {
                        view.tv_name.text = this.namaLengkap
                        view.tv_date.text = changeDateFormat(this.waktuUlasan,"yyyy-MM-dd HH:mm:ss","d MMMM yyyy")
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
        var login = sharedPreferenceUtil.getBoolean(SESSION)
        if (login) {
            getProfilePelanggan(sharedPreferenceUtil)?.let {
                pelanggan = it
                idPelanggan = pelanggan.id_pelanggan
            }
        }
        setLayout(login)
        idKatering = arguments.getInt(ID_KATERING)
        presenter?.getListMenuKatering(idKatering, idPelanggan)

        swipe_layout.setOnRefreshListener({
            presenter?.getListMenuKatering(idKatering, idPelanggan)
        })

        cv_add_review.setOnClickListener {
            var args = Bundle()
            args.putInt(ID_KATERING, idKatering)
            args.putInt(ID_PELANGGAN, pelanggan.id_pelanggan)
            args.putString(TYPE,ADD)
            val reviewDialog = ReviewDialog()
            reviewDialog.arguments = args
            reviewDialog.setTargetFragment(this@ReviewFragment, 1)
            reviewDialog.show(activity.supportFragmentManager, REVIEW)
        }

        ic_edit.setOnClickListener {
            var args = Bundle()
            args.putInt(ID_ULASAN,review.idUlasan)
            args.putFloat(RATING,review.rating)
            args.putString(ULASAN,review.ulasan)
            args.putString(TYPE,EDIT)
            val reviewDialog = ReviewDialog()
            reviewDialog.arguments = args
            reviewDialog.setTargetFragment(this@ReviewFragment, 1)
            reviewDialog.show(activity.supportFragmentManager, REVIEW)
        }

        ic_delete.setOnClickListener {
            activity.buildAlertDialog(getString(R.string.delete_review_dialog_title), yesButton = getString(R.string.yes_dialog), noButton = getString(R.string.no_dialog), positiveAction = {
                setHorizontalProgress(true)
                presenter?.deleteReview(review.idUlasan)
            }).show()
        }
    }

    fun setLayout(login: Boolean) {
        if (login) cv_add_review.show() else cv_add_review.hide()
    }

    fun setMyReview(rating: Float, ulasan: String) {
        rb_review.rating = rating
        tv_review.text = ulasan
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
                setMyReview(it.rating, it.ulasan)
                this.review = it
            }
            with(rv_review) {
                review?.let {
                    listReview.clear()
                    listReview.addAll(it.listulasan)
                }
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
            presenter?.getListMenuKatering(idKatering, idPelanggan)
        } else {
            activity.showShortToast(t?.localizedMessage!!)
        }
    }

    override fun onInsertReview(review: InsertReviewResponse) {
        setMyReview(review.rating, review.ulasan)
        this.review.idUlasan=review.idUlasan
        this.review.rating=review.rating
        this.review.ulasan= review.ulasan
        activity?.let {
            it.showShortToast(review.message)
        }
        presenter?.getListMenuKatering(idKatering, idPelanggan)
    }

    override fun onUpdateReview(review: UpdateReviewResponse) {
        setMyReview(review.rating, review.ulasan)
        this.review.idUlasan=review.idUlasan
        this.review.rating=review.rating
        this.review.ulasan= review.ulasan
        activity?.let {
            it.showShortToast(review.message)
        }
        presenter?.getListMenuKatering(idKatering, idPelanggan)
    }
}