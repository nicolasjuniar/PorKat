package juniar.porkat.transaction

/**
 * Created by Jarvis on 12/03/2018.
 */
interface TransactionView {
    fun setLoading(loading: Boolean)
    fun onGetDescriptionTransaction(startDate: String, orderDay: Int, transactionNumber: Int)
    fun onPickMenu(list: MutableList<PickMenuModel>)
    fun onPickPlace(address: String, note: String, longitude: Double, latitude: Double)
    fun onTransactionResponse(error: Boolean, message: String?, t: Throwable?)
}