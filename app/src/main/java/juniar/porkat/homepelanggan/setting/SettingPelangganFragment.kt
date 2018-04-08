package juniar.porkat.homepelanggan.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant.CommonStrings.Companion.PELANGGAN
import juniar.porkat.common.Constant.CommonStrings.Companion.ROLE
import juniar.porkat.homepelanggan.HomePelangganActivity.Companion.EDIT_PELANGGAN
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * Created by Nicolas Juniar on 22/02/2018.
 */
class SettingPelangganFragment : BaseFragment<Any>() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menu_edit_profile.setOnClickListener {
            activity.startActivityForResult(Intent(activity, EditProfilePelangganActivity::class.java),EDIT_PELANGGAN)
        }
        menu_change_password.setOnClickListener {
            val intent=Intent(activity,ChangePasswordActivity::class.java)
            intent.putExtra(ROLE,PELANGGAN)
            startActivity(intent)
        }
    }

}