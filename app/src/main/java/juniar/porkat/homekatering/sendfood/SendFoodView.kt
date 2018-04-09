package juniar.porkat.homekatering.sendfood

interface SendFoodView {
    fun onGetListSendFood(error: Boolean, listSendFood: MutableList<SendFoodModel>?, t: Throwable?)
}