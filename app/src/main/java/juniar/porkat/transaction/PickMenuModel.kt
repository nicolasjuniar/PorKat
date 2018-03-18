package juniar.porkat.transaction

import juniar.porkat.detailkatering.menu.MenuKateringModel

/**
 * Created by Jarvis on 18/03/2018.
 */
data class PickMenuModel(var menu: MenuKateringModel = MenuKateringModel(),
                         var delilveryTime: String = "",
                         var visibility: Boolean = false,
                         var picked: Boolean = false)