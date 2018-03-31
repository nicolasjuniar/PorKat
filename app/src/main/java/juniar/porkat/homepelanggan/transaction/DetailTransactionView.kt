package juniar.porkat.homepelanggan.transaction

/**
 * Created by Jarvis on 25/03/2018.
 */
interface DetailTransactionView {
    fun onGetListMenuTransaction(error: Boolean, listMenu: MutableList<MenuTransactionModel>?, t: Throwable?)
}