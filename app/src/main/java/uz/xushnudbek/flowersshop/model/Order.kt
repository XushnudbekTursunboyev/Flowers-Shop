package uz.xushnudbek.flowersshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.*


data class Order(
    val id: String,
    val date:Date,
    val totalPrice:String,
    val state:String
): Serializable {

    constructor():this("",Date(),"","")
}