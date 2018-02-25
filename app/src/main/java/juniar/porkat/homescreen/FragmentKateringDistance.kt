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
import android.widget.TextView
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.show
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.DetailKateringActivity
import kotlinx.android.synthetic.main.fragment_rating.*

/**
 * Created by Nicolas Juniar on 24/02/2018.
 */
class FragmentKateringDistance : BaseFragment<KateringPresenter>(), KateringView {

    lateinit var getKateringList: MutableList<GetKateringModel>
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    val myLocation= Location(Constant.CommonStrings.EMPTY_STRING)

    private val kateringAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_katering, getKateringList,
                { kateringModel, _, _ ->
                    startActivity(Intent(activity,DetailKateringActivity::class.java))
                },
                { kateringModel, view ->
                    with(kateringModel) {
                        val kateringLocation= Location(Constant.CommonStrings.EMPTY_STRING)
                        kateringLocation.longitude=kateringModel.longitude
                        kateringLocation.latitude=kateringModel.latitude
                        kateringModel.distance=myLocation.distanceTo(kateringLocation)/1000
                        view.findViewById<TextView>(R.id.tv_katering).text = this.nama_katering
                        view.findViewById<TextView>(R.id.tv_alamat).text = this.alamat
                        view.findViewById<TextView>(R.id.tv_jarak).text = "${this.distance.toString().substring(0,4)} km"
                        view.findViewById<TextView>(R.id.tv_rating).text = this.rating.toString()
                        Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/katering/${this.foto}").fit().into(view.findViewById<ImageView>(R.id.img_katering))
                    }
                })
    }

    override fun onGetListKatering(error: Boolean, response: ListKateringResponse?, t: Throwable?) {
        setLoading(false)
        swipe_layout.isRefreshing = false
        if (!error) {
            with(rv_katering) {
                getKateringList = response?.listkatering!!
                adapter = kateringAdapter
                layoutManager = LinearLayoutManager(activity)
            }
            kateringAdapter.notifyDataSetChanged()
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_rating, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = KateringPresenter(this)
        sharedPreferenceUtil= SharedPreferenceUtil(activity)
        getKateringList = mutableListOf()
        getMyLocation(sharedPreferenceUtil)
        myLocation.longitude=sharedPreferenceUtil.getString(Constant.CommonStrings.LONGITUDE,"0").toDouble()
        myLocation.latitude=sharedPreferenceUtil.getString(Constant.CommonStrings.LATITUDE,"0").toDouble()
        presenter?.getListKateringByDistance(myLocation.longitude,myLocation.latitude)

        swipe_layout.setOnRefreshListener({
            getMyLocation(sharedPreferenceUtil)
            myLocation.longitude=sharedPreferenceUtil.getString(Constant.CommonStrings.LONGITUDE,"0").toDouble()
            myLocation.latitude=sharedPreferenceUtil.getString(Constant.CommonStrings.LATITUDE,"0").toDouble()
            presenter?.getListKateringByDistance(myLocation.longitude,myLocation.latitude)
        })

        rv_katering.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                swipe_layout.isEnabled = topRowVerticalPosition >= 0
            }
        })
    }
}