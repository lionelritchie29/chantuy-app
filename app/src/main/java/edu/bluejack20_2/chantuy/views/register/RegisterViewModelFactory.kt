package edu.bluejack20_2.chantuy.views.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.bluejack20_2.chantuy.data.RegisterDataSource
import edu.bluejack20_2.chantuy.data.RegisterRepository

/**
 * ViewModel provider factory to instantiate RegisterViewModel.
 * Required given RegisterViewModel has a non-empty constructor
 */
class RegisterViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                registerRepository = RegisterRepository(
                    dataSource = RegisterDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}