package co.mvvm_dagger_iteo.ui.cars.viewModels

import androidx.lifecycle.ViewModel
import co.mvvm_dagger_iteo.data.PersonsRepository
import javax.inject.Inject

class PersonsViewModel(private val mPersonsRepository:PersonsRepository): ViewModel() {
    val lvdResponseError = mPersonsRepository.lvdResponseError
    val lvdlstPersons =  mPersonsRepository.lvdlstPersons

    fun getPersons() = mPersonsRepository.getPersons()
    fun getCachedOwners() = mPersonsRepository.getCachedPersonsData()
}