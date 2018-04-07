package juniar.porkat.homepelanggan.transaction.menu

import juniar.porkat.homepelanggan.transaction.MenuTransactionModel

interface MenuTransactionView{
    fun onGetListMenuTransaction(error: Boolean, listMenu: MutableList<MenuTransactionModel>?, t: Throwable?)
}