package juniar.porkat.detailkatering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import juniar.porkat.R
import juniar.porkat.auth.KateringModel
import juniar.porkat.common.BaseFragment
import juniar.porkat.detailkatering.DetailKateringActivity.Companion.DETAIL_KATERING
import juniar.porkat.homescreen.GetKateringModel
import kotlinx.android.synthetic.main.fragment_deskripsi.*

/**
 * Created by Nicolas Juniar on 24/02/2018.
 */
class DeskripsiKateringFragment:BaseFragment<Any>(){

    companion object {
        val DESKRIPSI="deskripsi"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_deskripsi, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val katering=Gson().fromJson(arguments.getString(DESKRIPSI),GetKateringModel::class.java)
        tv_address.text=katering.alamat
        tv_contact.text=katering.no_telp
        tv_rating.text=katering.rating.toString()
    }
}