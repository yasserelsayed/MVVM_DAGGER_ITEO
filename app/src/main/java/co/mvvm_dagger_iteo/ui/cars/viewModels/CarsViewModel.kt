package co.mvvm_dagger_iteo.ui.cars.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.data.CarsRepository
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.domain.AppError
import co.mvvm_dagger_iteo.domain.Car
import javax.inject.Inject

class CarsViewModel(private val mCarsRepository:CarsRepository,val mApp: App): ViewModel() {
    val lvdResponseError:LiveData<AppError> = mCarsRepository.lvdResponseError
    val lvdlstCars :LiveData<List<Car>> =  mCarsRepository.lvdlstCars
    val lvdCarObj:LiveData<Car?> = mCarsRepository.lvdCarObj

    fun getCars() = mCarsRepository.getCars()
    fun addCar(mCar: Car) = mCarsRepository.addCar(mCar)
    fun syncWithServer(mCar: Car):Int{
        return if(mApp.isNetworkAvailable()) {
            mCarsRepository.addCar(mCar)
            -1
        }else R.string.msg_this_action_not_available_offline
    }

}