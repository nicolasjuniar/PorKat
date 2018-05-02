package juniar.porkat.homekatering.sendfood

interface SendFoodMapView{
    fun onGetDirection(error:Boolean,direction:DirectionResponse?,t:Throwable?)
}