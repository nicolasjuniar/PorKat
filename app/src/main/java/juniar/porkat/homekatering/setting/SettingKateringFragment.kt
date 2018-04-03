package juniar.porkat.homekatering.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import juniar.porkat.R
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.Constant.CommonStrings.Companion.KATERING
import juniar.porkat.common.Constant.CommonStrings.Companion.ROLE
import juniar.porkat.homepelanggan.setting.ChangePasswordActivity
import juniar.porkat.homepelanggan.setting.EditProfileActivity
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingKateringFragment:BaseFragment<Any>(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menu_edit_profile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
        menu_change_password.setOnClickListener {
            val intent=Intent(activity,ChangePasswordActivity::class.java)
            intent.putExtra(ROLE,KATERING)
            startActivity(intent)
        }
    }
}