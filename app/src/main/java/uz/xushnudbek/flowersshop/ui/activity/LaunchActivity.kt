package uz.xushnudbek.flowersshop.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import uz.xushnudbek.flowersshop.R
import uz.xushnudbek.flowersshop.firebaseDatabase.FirebaseDb
import uz.xushnudbek.flowersshop.model.Product
import uz.xushnudbek.flowersshop.util.Constants.Companion.FURNITURE_CATEGORY
import uz.xushnudbek.flowersshop.viewmodel.lunchapp.KleineViewModel
import uz.xushnudbek.flowersshop.viewmodel.lunchapp.ViewModelProviderFactory

class LaunchActivity : AppCompatActivity() {
    val viewModel by lazy {
        val firebaseDb = FirebaseDb()
        val viewModelFactory = ViewModelProviderFactory(firebaseDb)
        ViewModelProvider(this,viewModelFactory)[KleineViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        supportActionBar?.hide()
      //  saveNewProduct()
    }

   // private fun saveNewProduct() {
//
//        val title = "Bedside tables"
//        val description = "Your bedroom is a sanctuary where you unwind and create your own personal space." +
//                " You deserve to have this space ornamented to help you stay organized, relaxed, and comfortable." +
//                " Bedroom furniture creates utility and helps you be your functional best"
//
//
//
//
//        val category = FURNITURE_CATEGORY
//        val price = "300"
//        val newPrice = "229"
//        val seller = "ps mart"
//        val orders = 3
//
//        val images = HashMap<String,Any>()
//        val imagesList = listOf(
//            "https://wakefit-co.s3.ap-south-1.amazonaws.com/img/product-thumbnails/elara-double-drawer-bedside-table-lifestyle-rectangle-new.webp",
//            "https://wakefit-co.s3.ap-south-1.amazonaws.com/img/product-thumbnails/elara-double-drawer-bedside-table-lifestyle-rectangle-new.webp",
//            "https://wakefit-co.s3.ap-south-1.amazonaws.com/img/product-thumbnails/elara-double-drawer-bedside-table-lifestyle-rectangle-new.webp"
//
//        )
//
//        images.put(IMAGES,imagesList.toList())
//
//
//        val prodcut = Product(1208025,title, description, category, newPrice,price, seller, images,orders,null)
//
//        Firebase.firestore.collection(PRODUCTS_COLLECTION)
//            .document()
//            .set(prodcut)
//    }

}