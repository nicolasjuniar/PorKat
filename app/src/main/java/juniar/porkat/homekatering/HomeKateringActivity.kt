package juniar.porkat.homekatering

import android.content.Intent
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import juniar.porkat.R
import juniar.porkat.Utils.SharedPreferenceUtil
import juniar.porkat.Utils.buildAlertDialog
import juniar.porkat.Utils.getProfileKatering
import juniar.porkat.Utils.showShortToast
import juniar.porkat.auth.KateringModel
import juniar.porkat.common.BaseActivity
import juniar.porkat.common.Constant
import juniar.porkat.common.Constant.CommonStrings.Companion.KATERING
import juniar.porkat.homekatering.menu.MenuKateringFragment
import juniar.porkat.homekatering.setting.SettingKateringFragment
import juniar.porkat.homescreen.HomeActivity
import kotlinx.android.synthetic.main.activity_home_katering.*
import kotlinx.android.synthetic.main.app_bar_home_katering.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class HomeKateringActivity : BaseActivity<Any>(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var fragmentManager: FragmentManager
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    var exit = false
    lateinit var katering: KateringModel

    override fun onSetupLayout() {
        setContentView(R.layout.activity_home_katering)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.send_food_text)
    }

    override fun onViewReady() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar_layout as Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        sharedPreferenceUtil = SharedPreferenceUtil(this@HomeKateringActivity)
        fragmentManager = supportFragmentManager
        katering = getProfileKatering(sharedPreferenceUtil)
        nav_view.menu.getItem(0).isChecked = true
        loadPreferences()
        nav_view.setNavigationItemSelectedListener(this)
    }

    fun loadPreferences() {
        val navView = nav_view.getHeaderView(0)
        navView.tv_fullname.setText(katering.namaKatering)
        navView.tv_role.setText(KATERING)
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
            R.id.nav_send_food -> {
            }
            R.id.nav_food -> {

            }
            R.id.nav_transaction -> {

            }
            R.id.nav_menu -> {
                changeTitleToolbar(getString(R.string.manage_menu))
                fragment = MenuKateringFragment()
            }
            R.id.nav_setting -> {
                changeTitleToolbar(R.string.setting_text)
                fragment = SettingKateringFragment()
            }
            R.id.nav_logout -> {
                buildAlertDialog(getString(R.string.logout_dialog_title), getString(R.string.logout_dialog_detail), getString(R.string.yes_dialog), getString(R.string.no_dialog), positiveAction = {
                    sharedPreferenceUtil.setBoolean(Constant.CommonStrings.SESSION, false)
                    startActivity(Intent(this@HomeKateringActivity, HomeActivity::class.java))
                    finish()
                }).show()
            }
        }

        fragment?.let {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, fragment)
                    .commit()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
