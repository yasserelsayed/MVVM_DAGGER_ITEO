package co.mvvm_dagger_iteo.ui.cars.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.mvvm_dagger_iteo.data.CarsRepository
import co.mvvm_dagger_iteo.domain.AppError
import co.mvvm_dagger_iteo.domain.Car
import javax.inject.Inject

class CarsViewModel @Inject constructor(private val mCarsRepository:CarsRepository): ViewModel() {
    val lvdResponseError:LiveData<AppError> = mCarsRepository.lvdResponseError
    val lvdlstCars :LiveData<List<Car>> =  mCarsRepository.lvdlstCars
    val lvdCarObj:LiveData<Car?> = mCarsRepository.lvdCarObj

    fun getCars() = mCarsRepository.getCars()
    fun addCar(mCar: Car) = mCarsRepository.addCar(mCar)

}