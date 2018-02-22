package juniar.porkat.homepelanggan

import android.content.Intent
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.google.gson.Gson
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.buildAlertDialog
import juniar.porkat.Utils.showShortToast
import juniar.porkat.auth.PelangganModel
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant.CommonStrings.Companion.PELANGGAN
import juniar.porkat.common.Constant.CommonStrings.Companion.PROFILE_PELANGGAN
import juniar.porkat.common.Constant.CommonStrings.Companion.SESSION
import juniar.porkat.homepelanggan.home.HomePelangganFragment
import juniar.porkat.homepelanggan.setting.SettingFragment
import juniar.porkat.homescreen.HomeActivity
import kotlinx.android.synthetic.main.activity_home_pelanggan.*
import kotlinx.android.synthetic.main.app_bar_home_pelanggan.*
import kotlinx.android.synthetic.main.nav_header_home_pelanggan.view.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class HomePelangganActivity : BaseActivity<Any>(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    lateinit var fragmentManager: FragmentManager
    var exit = false
    lateinit var pelanggan: PelangganModel

    override fun onSetupLayout() {
        setContentView(R.layout.activity_home_pelanggan)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.home_text)
    }

    override fun onViewReady() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar_layout as Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        sharedPreferenceUtil = SharedPreferenceUtil(this@HomePelangganActivity)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container_body, HomePelangganFragment()).commit()
        pelanggan = Gson().fromJson(sharedPreferenceUtil.getString(PROFILE_PELANGGAN), PelangganModel::class.java)
        nav_view.menu.getItem(0).isChecked = true
        loadPreferences()
        nav_view.setNavigationItemSelectedListener(this)
    }

    fun loadPreferences() {
        val navView = nav_view.getHeaderView(0)
        navView.tv_fullname.setText(pelanggan.nama_lengkap)
        navView.tv_role.setText(PELANGGAN)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (exit) {
                finish()
            } else {
                this.showShortToast(getString(R.string.exit_text))
                exit = true
                Handler().postDelayed({ exit = false }, (3 * 1000).toLong())
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_home -> {
                changeTitleToolbar(R.string.home_text)
                fragment = HomePelangganFragment()
            }
            R.id.nav_transaction -> {

            }
            R.id.nav_setting -> {
                changeTitleToolbar(R.string.setting_text)
                fragment = SettingFragment()

            }
            R.id.nav_logout -> {
                buildAlertDialog(getString(R.string.logout_dialog_title), getString(R.string.logout_dialog_detail), getString(R.string.yes_dialog), getString(R.string.no_dialog), positiveAction = {
                    sharedPreferenceUtil.setBoolean(SESSION, false)
                    startActivity(Intent(this@HomePelangganActivity, HomeActivity::class.java))
                    finish()
                }).show()
            }
        }

        fragment?.let {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, fragment).commit()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
