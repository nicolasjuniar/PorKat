package juniar.porkat.homekatering.menu

interface EditMenuView{
    fun onInsertMenu(error:Boolean,message:String?,t:Throwable?)
    fun setLoading(loading:Boolean)
    fun onUpdateMenu(error:Boolean,message:String?,t:Throwable?)
    fun onDeleteMenu(error:Boolean,message:String?,t:Throwable?)
}