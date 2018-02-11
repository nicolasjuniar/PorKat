package juniar.porkat.homescreen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
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
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.show
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.GeneralRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_katering_rating.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class FragmentKateringRating : Fragment(), KateringView {

    lateinit var getKateringList: MutableList<GetKateringModel>
    lateinit var presenter: KateringPresenter

    private val kateringAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_katering, getKateringList,
                { kateringModel, _, _ ->

                },
                { kateringModel, view ->
                    with(kateringModel) {
                        view.findViewById<TextView>(R.id.tv_katering).text = this.nama_katering
                        view.findViewById<TextView>(R.id.tv_alamat).text = this.alamat
                        view.findViewById<TextView>(R.id.tv_jarak).text = "${this.distance.toString()} km"
                        view.findViewById<TextView>(R.id.tv_rating).text = this.rating.toString()
                        Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/katering/${this.foto}").fit().into(view.findViewById<ImageView>(R.id.img_katering))
                    }
                })
    }

    override fun onGetListKatering(error: Boolean, response: ListKateringResponse?, t: Throwable?) {
        setLoading(false)
        if (!error) {
            with(rv_katering) {
                getKateringList = response?.listkatering!!
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_katering_rating, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = KateringPresenter(this)
        getKateringList = mutableListOf()
        presenter?.getListKatering()

        swipe_layout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            presenter?.getListKatering()
        })

        rv_katering.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                swipe_layout.isEnabled = topRowVerticalPosition >= 0
            }
        })
    }
}