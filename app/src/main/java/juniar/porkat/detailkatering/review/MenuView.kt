package juniar.porkat.detailkatering.review

import juniar.porkat.detailkatering.menu.MenuKateringModel

/**
 * Created by Nicolas Juniar on 26/02/2018.
 */
interface MenuView {
    fun setLoading(loading: Boolean)
    fun onGetListMenu(error: Boolean, list: MutableList<MenuKateringModel>?, t: Throwable?)
}