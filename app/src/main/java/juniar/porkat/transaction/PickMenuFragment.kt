package juniar.porkat.transaction

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import juniar.porkat.PorkatApp
import juniar.porkat.R
import juniar.porkat.Utils.convertToIDR
import juniar.porkat.Utils.hide
import juniar.porkat.Utils.show
import juniar.porkat.Utils.showShortToast
import juniar.porkat.common.BaseFragment
import juniar.porkat.common.GeneralRecyclerViewAdapter
import juniar.porkat.detailkatering.menu.MenuFragment.Companion.ID_KATERING
import juniar.porkat.detailkatering.menu.MenuKateringModel
import juniar.porkat.transaction.PickMenuActivity.Companion.PICKED_MENU
import kotlinx.android.synthetic.main.fragment_pick_menu.*
import kotlinx.android.synthetic.main.viewholder_pick_menu.view.*
import java.util.*

/**
 * Created by Jarvis on 18/03/2018.
 */
class PickMenuFragment : BaseFragment<Any>() {

    var listPickMenu = mutableListOf<PickMenuModel>()
    var picked = -1
    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendar: Calendar
    var hour = -1
    var minute = -1
    lateinit var callback: TransactionView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as TransactionView
    }

    private val pickMenuAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.viewholder_pick_menu, listPickMenu,
                { _, position, _ ->
                    picked = position
                    val intent = Intent(activity, PickMenuActivity::class.java)
                    intent.putExtra(ID_KATERING, arguments.getInt(ID_KATERING))
                    startActivityForResult(intent, PICK_MENU)
                },
                { pickMenu, view ->
                    with(pickMenu) {
                        if (pickMenu.visibility) {
                            view.pick_menu.show()
                            if (pickMenu.picked) {
                                view.iv_pick_menu.hide()
                                view.cv_picked_menu.show()
                                view.tv_time.setOnClickListener {
                                    timePickerDialog = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                                        view.tv_time.text = "$hour : $minute"
                                        listPickMenu[listPickMenu.indexOf(pickMenu)].delilveryTime = "$hour:$minute"
                                        callback.onPickMenu(listPickMenu)
                                    }, hour, minute, false)
                                    timePickerDialog.show()
                                }
                                with(pickMenu.menu) {
                                    view.tv_menu.text = this.namaMenu
                                    view.tv_price.text = this.harga.toString().convertToIDR()
                                    Picasso.with(activity).load("${PorkatApp.BASE_URL}/foto/menu/${this.foto}").centerCrop().resize(200, 200).into(view.iv_menu)
                                }
                            } else {
                                view.cv_picked_menu.hide()
                                view.iv_pick_menu.show()
                            }
                        } else {
                            view.pick_menu.hide()
                        }
                    }
                })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            callback.onPickMenu(listPickMenu)
        }
    }

    companion object {
        val PICK_MENU = 1
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_pick_menu, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listPickMenu.add(PickMenuModel())
        listPickMenu.add(PickMenuModel())
        listPickMenu.add(PickMenuModel())
        calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        with(rv_menu) {
            adapter = pickMenuAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    fun changeAdapterSize(size: Int) {
        listPickMenu.forEachIndexed { index, pickMenuModel ->
            pickMenuModel.visibility = index <= size
        }
        pickMenuAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_MENU) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    listPickMenu[picked].menu = it.extras.get(PICKED_MENU) as MenuKateringModel
                }
                listPickMenu[picked].picked = true
                pickMenuAdapter.notifyDataSetChanged()
            }
        }
    }
}