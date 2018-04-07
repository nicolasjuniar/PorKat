package juniar.porkat.homepelanggan.transaction.invoice

interface InvoiceView{
    fun setLoading(loading:Boolean)
    fun onSuccessUpdateInvoice(error:Boolean,message:String?,t:Throwable?)
}