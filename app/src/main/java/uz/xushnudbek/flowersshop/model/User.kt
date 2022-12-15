package uz.xushnudbek.flowersshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class User(
    var firstName:String,
    var lastName:String,
    var email:String,
    var imagePath:String=""
): Serializable {

    constructor() : this("","","")
}

