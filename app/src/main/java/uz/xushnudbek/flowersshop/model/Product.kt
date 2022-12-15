package uz.xushnudbek.flowersshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.*

data class Product(
    val id:Int,
    val title: String? = "",
    val description: String? = "",
    val category: String? = "",
    val newPrice:String?="",
    val price: String? = "",
    val seller: String? = "",

    val images: String= "",
    val orders:Int = 0,
    val offerTime:Date? = null,
    val sizeUnit:String?=null

) : Serializable
    {
    constructor(
         id :Int,
         title: String? = "",
         description: String? = "",
         category: String? = "",
         price: String? = "",
         seller: String? = "",
         images: String = "",
    ) : this(id,title,description,category,null,price,seller, images, 0,null,null)
    }
