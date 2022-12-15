package uz.xushnudbek.flowersshop.model

import java.io.Serializable


data class Address(
    val addressTitle:String,
    val fullName:String,
    val street:String,
    val phone:String,
    val city:String,
    val state:String
) : Serializable {
    constructor():this("","","","","","")
}