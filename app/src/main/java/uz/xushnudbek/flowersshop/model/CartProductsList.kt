package uz.xushnudbek.flowersshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

data class CartProductsList(
    val products:List<CartProduct>
) : Serializable {


}
