package juniar.porkat.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.common.BaseFragment

/**
 * Created by Nicolas Juniar on 14/02/2018.
 */
class FillAutPelangganFragment : BaseFragment<Any>() {

    companion object {
        fun newInstance(): FillAutPelangganFragment {
            return FillAutPelangganFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_register_autentikasi, container, false)

    interface fillFieldCallback {
        fun onFieldFilled(enable: Boolean)
    }
}