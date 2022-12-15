package uz.xushnudbek.flowersshop.model

data class CartProduct (
    val id:Int,
    val name:String,
    val store:String,
    val image:String,
    val price:String,
    val newPrice:String?,
    val quantity:Int,
        ) {
    constructor() : this(0,"","","","","",0)
}
