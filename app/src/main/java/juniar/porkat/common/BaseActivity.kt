package juniar.porkat.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import juniar.porkat.R
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by Nicolas Juniar on 12/02/2018.
 */
abstract class BaseActivity<T> : AppCompatActivity() {

    var presenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSetupLayout()
        onViewReady()
    }

    fun setupToolbarTitle(toolbarId: Toolbar, title: Int = R.string.empty_string, drawable: Int = R.drawable.ic_back_24dp) {
        setSupportActionBar(toolbarId)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setHomeAsUpIndicator(drawable)
        }
        toolbar_title.setText(title)
    }

    fun changeTitleToolbar(title: Int) {
        toolbar_title.setText(title)
    }

    fun setupToolbarTitleNoBack(toolbarId: Toolbar, title: Int = R.string.empty_string) {
        setSupportActionBar(toolbarId)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
        }
        toolbar_title.setText(title)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.let {
            (presenter as BasePresenter).clearCompositeDisposable()
        }
    }

    protected abstract fun onSetupLayout()
    protected abstract fun onViewReady()
}
