package co.mvvm_dagger_iteo.data

import androidx.lifecycle.MutableLiveData
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.models.AddCarReponse
import co.mvvm_dagger_iteo.data.remote.CarService
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.domain.AppError
import co.mvvm_dagger_iteo.domain.Car
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

class CarsRepository @Inject constructor(private val mCarService: CarService, private val mAppDatabase: AppDatabase, private val mApp: App, val mAppSession: AppSession) {
    val lvdResponseError = MutableLiveData<AppError>()
    val lvdlstCars= MutableLiveData<List<Car>>()
    val lvdCarObj= MutableLiveData<Car?>()

    fun getCars(){
        if(mApp.isNetworkAvailable()) {
            mCarService.getCars().enqueue(object : Callback<List<co.mvvm_dagger_iteo.data.models.Car>> {
                override fun onResponse(call: Call<List<co.mvvm_dagger_iteo.data.models.Car>>, response: retrofit2.Response<List<co.mvvm_dagger_iteo.data.models.Car>>) {
                    val cars = response.body()
                    cars?.let{
                        it.forEach { car ->
                          if(!updateWith(car))
                             mAppDatabase.carDao().insertCar(car)
                        }
                    }
                    lvdlstCars.value = mAppDatabase.carDao().gelAllCars()?.map { Car(it) }
                }

                override fun onFailure(call: Call<List<co.mvvm_dagger_iteo.data.models.Car>>, t: Throwable) {
                    lvdResponseError.value = AppError(t.localizedMessage)
                }
            })
        }else lvdlstCars.value  =  mAppDatabase.carDao().gelAllCars().map { Car(it) }
    }

    fun addCar(m:Car){
        if(mApp.isNetworkAvailable()) {
        mCarService.addCar(m.getDataObj()).enqueue(object : Callback<AddCarReponse> {
            override fun onResponse(call: Call<AddCarReponse>, response: retrofit2.Response<AddCarReponse>) {
                lvdCarObj.value = response.body()?.let {
                    Car(it.brand,it.color,0.0,0.0,it.model,"",it.registration,it.year)
                }?:null
            }
            override fun onFailure(call: Call<AddCarReponse>, t: Throwable) {
                lvdResponseError.value = AppError(t.localizedMessage)
            }
        })
        }else mAppDatabase.carDao().insertCar(m.getDataObj().apply { synced = false })
    }

  private fun updateWith(m: co.mvvm_dagger_iteo.data.models.Car): Boolean {
        if (m?._id.isNullOrEmpty()) return true
        val cars = mAppDatabase.carDao().getCarByID(m._id!!)
        cars.firstOrNull()?.let {
            mAppDatabase.carDao().updateCar(it.apply {
                brand = m.brand
                color = m.color
                year = m.year
                lat = m.lat
                lng = m.lng
                ownerId = m.ownerId
                registration = m.registration
            })
            return true
        } ?: return false
    }
}