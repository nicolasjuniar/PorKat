package juniar.porkat.homekatering

import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import juniar.porkat.R
import juniar.porkat.common.BaseActivity
import kotlinx.android.synthetic.main.activity_home_katering.*
import kotlinx.android.synthetic.main.app_bar_home_katering.*

class HomeKateringActivity : BaseActivity<Any>(), NavigationView.OnNavigationItemSelectedListener {

    override fun onSetupLayout() {
        setContentView(R.layout.activity_home_katering)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.home_text)
    }

    override fun onViewReady() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar_layout as Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_katering, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
