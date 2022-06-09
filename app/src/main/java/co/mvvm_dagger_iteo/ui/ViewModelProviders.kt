package co.mvvm_dagger_iteo.ui
import androidx.lifecycle.ViewModelProvider

interface ViewModelProviders {
    interface CarsViewModelFactory: ViewModelProvider.Factory
    interface PersonsViewModelFactory: ViewModelProvider.Factory
}