package juniar.porkat.transaction

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import juniar.porkat.R
import juniar.porkat.Utils.*
import juniar.porkat.common.BaseActivity
import kotlinx.android.synthetic.main.activity_transaction.*

/**
 * Created by Jarvis on 12/03/2018.
 */
class TransactionActivity : BaseActivity<Any>(), ViewPager.OnPageChangeListener,TransactionView {

    val fragmentList = mutableListOf<Fragment>()
    lateinit var slider: SliderPagerAdapter
    var indicators = arrayListOf<ImageView>()
//    var listBoolean = mutableListOf(false, false, false)

    companion object {
        val NUM_PAGES = 3
    }

    override fun onSetupLayout() {
        setContentView(R.layout.activity_transaction)
        setupToolbarTitle(toolbar_layout as Toolbar, R.string.description_transaction)
    }

    override fun onViewReady() {
        fragmentList.add(DescriptionTransactionFragment())
        fragmentList.add(DescriptionTransactionFragment())
        fragmentList.add(DescriptionTransactionFragment())
        slider = SliderPagerAdapter(supportFragmentManager, fragmentList)
        with(viewpager) {
            adapter = slider
            addOnPageChangeListener(this@TransactionActivity)
        }
        setupPagerIndicator(NUM_PAGES)
        btn_submit.setAvailable(true, this@TransactionActivity)
    }

    override fun onBackPressed() {
        if (viewpager.currentItem != 0) {
            viewpager.currentItem--
            btn_submit.setAvailable(true, this@TransactionActivity)
        } else {
            super.onBackPressed()
        }
    }

    fun setLayout(index: Int) {
        if (index == 0) {
            changeTitleToolbar(R.string.autentikasi_text)
            with(btn_submit) {
                text = getString(R.string.next_text)
                setOnClickListener {
                    btn_submit.setAvailable(true, this@TransactionActivity)
                    viewpager.currentItem++
                }
            }
        } else {
            changeTitleToolbar(R.string.private_text)
            with(btn_submit) {
                text = getString(R.string.register_text)
                setOnClickListener {
                }
            }
        }
    }

    private fun setupPagerIndicator(size: Int) {
        for (i in 0 until size) {
            indicators.add(ImageView(this@TransactionActivity))
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(this@TransactionActivity,
                            if (i == 0) R.drawable.dot_indicator else R.drawable.dot_indicator_disabled)
            )

            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.LEFT
            }

            val margin = resources.getDimensionPixelSize(R.dimen.dimen_3dp)
            params.setMargins(
                    if (i != 0) margin else 0,
                    0,
                    margin,
                    0
            )
            layout_indicator.addView(indicators[i], params)
        }
        indicators[0].isSelected = true
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            window.setFlags(DONT_TOUCH, DONT_TOUCH)
            progressbar.show()
        } else {
            window.clearFlags(DONT_TOUCH)
            progressbar.hide()
        }
    }

    override fun onGetDescriptionTransaction(startDate: String, orderDay: Int, transactionNumber: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        setLayout(position)
    }

    override fun onPageSelected(position: Int) {
        for (i in 0 until NUM_PAGES) {
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this@TransactionActivity, R.drawable.dot_indicator_disabled))
        }
        indicators[position].setImageDrawable(ContextCompat.getDrawable(this@TransactionActivity, R.drawable.dot_indicator))
    }

}