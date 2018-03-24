package juniar.porkat.homepelanggan.transaction

/**
 * Created by Jarvis on 22/03/2018.
 */
interface HistoryTransactionView{
    fun setLoading(loading:Boolean)
    fun onGetListTransactionPelanggan(error:Boolean, listTransaction:MutableList<GetTransactionModel>?, t:Throwable?)
}