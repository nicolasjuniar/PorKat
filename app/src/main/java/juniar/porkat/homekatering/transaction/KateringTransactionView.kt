package juniar.porkat.homekatering.transaction

interface KateringTransactionView{
    fun setLoading(loading:Boolean)
    fun onGetListTransactionKatering(error:Boolean, listTransaction:MutableList<KateringTransactionModel>?,t:Throwable?)
}