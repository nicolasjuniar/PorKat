package juniar.porkat.homepelanggan.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
class SettingFragment : BaseFragment<Any>() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menu_edit_profile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
        menu_change_password.setOnClickListener {
            startActivity(Intent(activity,ChangePasswordActivity::class.java))
        }
    }

}