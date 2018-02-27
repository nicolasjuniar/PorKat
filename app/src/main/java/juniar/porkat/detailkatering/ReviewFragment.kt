package juniar.porkat.detailkatering

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.auth.PelangganModel
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.MenuFragment.Companion.ID_KATERING
import kotlinx.android.synthetic.main.fragment_review.*

/**
 * Created by Nicolas Juniar on 27/02/2018.
 */
class ReviewFragment : BaseFragment<ReviewPresenter>(), ReviewView {

    lateinit var listReview: MutableList<ReviewModel>
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    lateinit var pelanggan: PelangganModel

    private val reviewAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_review, listReview,
                { review, _, _ ->
                },
                { review, view ->
                    with(review) {
                        view.findViewById<TextView>(R.id.tv_name).text = this.namaLengkap
                        view.findViewById<TextView>(R.id.tv_date).text = changeDateFormat(this.waktuUlasan)
                        view.findViewById<RatingBar>(R.id.rb_review).rating = this.rating
                        view.findViewById<TextView>(R.id.tv_review).text = this.ulasan
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
            ReviewDialog().show(activity.fragmentManager,"Review")
        }
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            progressbar.show()
        } else {
            progressbar.hide()
        }
    }

    override fun onGetListReview(error: Boolean, list: MutableList<ReviewModel>?, t: Throwable?) {
        setLoading(false)
        swipe_layout.isRefreshing = false
        if (!error) {
            with(rv_review) {
                listReview = list!!
                adapter = reviewAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        } else {
            context.showShortToast(t?.localizedMessage!!)
        }
    }

}