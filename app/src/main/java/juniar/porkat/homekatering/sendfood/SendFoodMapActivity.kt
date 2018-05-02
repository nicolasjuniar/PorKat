package juniar.porkat.homekatering.sendfood

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.Toolbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.LATITUDE
import juniar.porkat.common.Constant.CommonStrings.Companion.LONGITUDE
import juniar.porkat.homekatering.sendfood.DetailSendFoodActivity.Companion.DETAIL_SEND_FOOD
import juniar.porkat.homekatering.sendfood.DetailSendFoodActivity.Companion.DETAIL_SEND_FOOD_CODE
import juniar.porkat.homekatering.sendfood.SendFoodFragment.Companion.LIST_SEND_FOOD
import kotlinx.android.synthetic.main.activity_send_food_map.*

class SendFoodMapActivity : BaseActivity<SendFoodMapPresenter>(), OnMapReadyCallback, SendFoodMapView {

    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    private lateinit var map: GoogleMap
    lateinit var listSendFood: MutableList<SendFoodModel>
    lateinit var polyline: Polyline
    private val listMarker = mutableListOf<Marker>()
    private var lastTag=-1

    companion object {
        const val SEND_FOOD=1
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_send_food_map)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.send_food_text)
    }

    override fun onViewReady() {
        sharedPreferenceUtil = SharedPreferenceUtil(this@SendFoodMapActivity)
        presenter = SendFoodMapPresenter(this)
        listSendFood = intent.getParcelableArrayListExtra(LIST_SEND_FOOD)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val myLongitude = sharedPreferenceUtil.getString(LONGITUDE).toDouble()
        val myLatitude = sharedPreferenceUtil.getString(LATITUDE).toDouble()
        map.addMarker(MarkerOptions()
                .position(LatLng(myLatitude, myLongitude))
                .icon(getMarkerFromDrawable(R.drawable.ic_my_location_24dp))
                .title("Lokasiku"))
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(myLatitude, myLongitude), 16.0f))
        listSendFood.forEachIndexed { index, it ->
            presenter?.getDirection(LatLng(myLatitude, myLongitude), LatLng(it.latitude, it.longitude))
            var marker = this.map.addMarker(MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .icon(getMarkerFromDrawable(R.drawable.ic_send_red_24dp))
                    .title(it.namaLengkap))
            marker.tag = index
            listMarker.add(marker)
        }

        map.setOnMarkerClickListener {
            it.tag?.let {
                lastTag=it as Int
                startActivityForResult(Intent(this@SendFoodMapActivity, DetailSendFoodActivity::class.java)
                        .putExtra(DETAIL_SEND_FOOD, listSendFood[lastTag]),DETAIL_SEND_FOOD_CODE)
            }
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==DETAIL_SEND_FOOD_CODE && resultCode== Activity.RESULT_OK){
            listMarker.forEach {
                if(it.tag==lastTag){
                    it.setIcon(getMarkerFromDrawable(R.drawable.ic_beenhere_24dp))
                    listSendFood[lastTag].status=getString(R.string.done_send_text)
                }
            }
        }
    }

    override fun onGetDirection(error: Boolean, direction: DirectionResponse?, t: Throwable?) {
        if (!error) {
            direction?.let {
                it.routes.forEachIndexed { _, _ ->
                    val polylineEncoded = it.routes[0].overviewPolyline.points
                    val listPolyline = decodePoly(polylineEncoded)
                    polyline = map.addPolyline(PolylineOptions()
                            .addAll(listPolyline)
                            .width(10f)
                            .color(R.color.polyline_color)
                            .geodesic(true))
                }
            }
        } else {
            t?.let {
                showShortToast(it.localizedMessage)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_OK)
    }

}
