package juniar.porkat.homekatering.food

interface FoodByDateView{
    fun onGetListMakanan(error:Boolean, listMenu:MutableList<MenuByDateModel>?, t:Throwable?)
}