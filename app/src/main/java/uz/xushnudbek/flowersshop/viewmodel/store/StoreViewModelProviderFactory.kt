package uz.xushnudbek.flowersshop.viewmodel.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.xushnudbek.flowersshop.firebaseDatabase.FirebaseDb

class StoreViewModelProviderFactory(
    val db:FirebaseDb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StoreViewModel(db) as T
    }
}