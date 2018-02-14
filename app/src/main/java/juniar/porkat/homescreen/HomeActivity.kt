package juniar.porkat.homescreen

import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import juniar.porkat.R
import juniar.porkat.Utils.TabPagerAdapter
import juniar.porkat.common.BaseActivity
import juniar.porkat.auth.login.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
class HomeActivity : BaseActivity<Any>() {

    var tabAdapter= TabPagerAdapter(supportFragmentManager)

    override fun onSetupLayout() {
        setContentView(R.layout.activity_home)
        setupToolbarTitleNoBack(toolbar_layout as Toolbar, R.string.home_text)
    }

    override fun onViewReady() {
        tabAdapter.addFragment(FragmentKateringRating(),"Rekomendasi")
        tabAdapter.addFragment(FragmentKateringRating(),"Sekitar")
        tabpager.adapter=tabAdapter
        tab_layout.setupWithViewPager(tabpager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_login -> {
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}