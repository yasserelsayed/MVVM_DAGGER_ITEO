package co.mvvm_dagger_iteo.ui.viewModels

import androidx.lifecycle.ViewModel
import co.mvvm_dagger_iteo.data.CarsRepository
import co.mvvm_dagger_iteo.domain.Car
import javax.inject.Inject

class CarsViewModel @Inject constructor(private val mCarsRepository:CarsRepository): ViewModel() {
    val lvdResponseError = mCarsRepository.lvdResponseError
    val lvdlstCars =  mCarsRepository.lvdlstCars
    val lvdCarObj= mCarsRepository.lvdCarObj

    fun getCars() = mCarsRepository.getCars()
    fun addCar(mCar: Car) = mCarsRepository.addCar(mCar)

}