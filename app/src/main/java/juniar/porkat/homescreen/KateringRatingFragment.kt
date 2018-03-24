package juniar.porkat.homescreen

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.show
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant.CommonStrings.Companion.EMPTY_STRING
import juniar.porkat.common.Constant.CommonStrings.Companion.LATITUDE
import juniar.porkat.common.Constant.CommonStrings.Companion.LONGITUDE
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.DetailKateringActivity
import juniar.porkat.detailkatering.DetailKateringActivity.Companion.DETAIL_KATERING
import kotlinx.android.synthetic.main.fragment_katering.*
import kotlinx.android.synthetic.main.viewholder_katering.view.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class KateringRatingFragment : BaseFragment<KateringPresenter>(), KateringView {

    lateinit var getKateringList: MutableList<GetKateringModel>
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    private val kateringAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_katering, getKateringList,
                { kateringModel, _, _ ->
                    val intent = Intent(activity, DetailKateringActivity::class.java)
                    intent.putExtra(DETAIL_KATERING, kateringModel)
                    startActivity(intent)
                },
                { kateringModel, view ->
                    with(kateringModel) {
                        val kateringLocation = Location(EMPTY_STRING)
                        kateringLocation.longitude = kateringModel.longitude
                        kateringLocation.latitude = kateringModel.latitude
                        val myLocation = Location(EMPTY_STRING)
                        myLocation.longitude = sharedPreferenceUtil.getString(LONGITUDE, "0").toDouble()
                        myLocation.latitude = sharedPreferenceUtil.getString(LATITUDE, "0").toDouble()
                        kateringModel.distance = myLocation.distanceTo(kateringLocation) / 1000
                        view.tv_katering.text = this.namaKatering
                        view.tv_alamat.text = this.alamat
                        view.tv_jarak.text = "${this.distance.toString().substring(0, 4)} km"
                        view.tv_rating.text = this.rating.toString()
                        Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/katering/${this.foto}").fit().into(view.findViewById<ImageView>(R.id.img_katering))
                    }
                })
    }

    override fun onGetListKatering(error: Boolean, response: ListKateringResponse?, t: Throwable?) {
        setLoading(false)
        swipe_layout.isRefreshing = false
        if (!error) {
            with(rv_katering) {
                getKateringList.clear()
                response?.let {
                    getKateringList.addAll(it.listKatering)
                }
                kateringAdapter.notifyDataSetChanged()
                adapter = kateringAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        } else {
            context.showShortToast(t?.localizedMessage!!)
        }
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            progressbar.show()
        } else {
            progressbar.hide()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_katering, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = KateringPresenter(this)
        sharedPreferenceUtil = SharedPreferenceUtil(activity)
        getKateringList = mutableListOf()
        setMyLocation(sharedPreferenceUtil)
        presenter?.getListKateringByRating()

        swipe_layout.setOnRefreshListener({
            presenter?.getListKateringByRating()
            setMyLocation(sharedPreferenceUtil)
        })

        rv_katering.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                swipe_layout.isEnabled = topRowVerticalPosition >= 0
            }
        })
    }
}