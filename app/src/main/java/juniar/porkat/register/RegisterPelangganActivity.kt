package juniar.porkat.register

import android.support.v7.widget.Toolbar
import juniar.porkat.R
import juniar.porkat.common.BaseActivity
import kotlinx.android.synthetic.main.activity_register_pelanggan.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class RegisterPelangganActivity : BaseActivity<Any>() {
    override fun onSetupLayout() {
        setContentView(R.layout.activity_register_pelanggan)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.register_text)
    }

    override fun onViewReady() {
    }

}