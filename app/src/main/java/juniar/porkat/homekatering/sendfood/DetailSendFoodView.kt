package juniar.porkat.homekatering.sendfood

interface DetailSendFoodView{
    fun setLoading(loading:Boolean)
    fun onDoneSendFood(error:Boolean,message:String?,throwable: Throwable?)
}