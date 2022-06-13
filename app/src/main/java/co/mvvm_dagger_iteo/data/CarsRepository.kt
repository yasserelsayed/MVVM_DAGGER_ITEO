package co.mvvm_dagger_iteo.data

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.models.AddCarReponse
import co.mvvm_dagger_iteo.data.remote.CarService
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.domain.AppError
import co.mvvm_dagger_iteo.domain.Car
import co.mvvm_dagger_iteo.domain.Person
import retrofit2.Call
import retrofit2.Callback
import java.util.concurrent.Executors
import javax.inject.Inject

class CarsRepository @Inject constructor(
    val mCarService: CarService,
    private val mAppDatabase: AppDatabase,
    private val mApp: App,
    private val mAppSession: AppSession
) {
    val lvdResponseError = MutableLiveData<AppError>()
    val lvdlstCars = MutableLiveData<List<Car>>()
    val lvdCarObj = MutableLiveData<Car?>()
    var testMode = false

    fun getCars() {
        if (mApp.isNetworkAvailable()) {
            mCarService.getCars()
                .enqueue(object : Callback<List<co.mvvm_dagger_iteo.data.models.Car>> {
                    override fun onResponse(
                        call: Call<List<co.mvvm_dagger_iteo.data.models.Car>>,
                        response: retrofit2.Response<List<co.mvvm_dagger_iteo.data.models.Car>>
                    ) {
                        val cars = response.body()
                        Executors.newSingleThreadExecutor().execute {
                            val owners = mAppDatabase.personDao().gelAllPersons()?.map { Person(it) }
                            cars?.let {
                                it.forEach { car ->
                                    if (!updateWith(car))
                                        mAppDatabase.carDao().insertCar(car.apply { synced = true })
                                }
                                var lstAllCars = mAppDatabase.carDao().gelAllCars()?.map { Car(it) }
                                 lstAllCars = attachOwnersToCars(owners,lstAllCars)
                                if (testMode) lvdlstCars.value = lstAllCars
                                else {
                                    Handler(Looper.getMainLooper()).post {
                                        lvdlstCars.value = lstAllCars
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<List<co.mvvm_dagger_iteo.data.models.Car>>,
                        t: Throwable
                    ) {
                        lvdResponseError.value = AppError(t.localizedMessage)
                    }
                })
        } else {
            Executors.newSingleThreadExecutor().execute {
                val cars = mAppDatabase.carDao().gelAllCars().map { Car(it) }
                val owners = mAppDatabase.personDao().gelAllPersons()?.map { Person(it) }
                val lstAllCars = attachOwnersToCars(owners, cars)
                if (testMode) lvdlstCars.value = lstAllCars
                else {
                    Handler(Looper.getMainLooper()).post {
                        lvdlstCars.value = lstAllCars
                    }
                }
            }
        }
    }

    private fun attachOwnersToCars(
        owners: List<Person>,
        cars: List<Car>
    ): List<Car> {
        val lstAllCars = cars?.map { c ->
            c.attachOwner(owners?.firstOrNull { it._id == c.ownerId })
            c
        }
        return lstAllCars
    }

    fun addCar(mCar: Car) {
        if (mApp.isNetworkAvailable()) {
            mCarService.addCar(mCar.getDataObj()).enqueue(object : Callback<AddCarReponse> {
                override fun onResponse(
                    call: Call<AddCarReponse>,
                    response: retrofit2.Response<AddCarReponse>
                ) {
                    lvdCarObj.value = response.body()?.let {
                        Car(
                            it.brand,
                            it.color,
                            mCar.lat,
                            mCar.lng,
                            it.model,
                            mCar.ownerId,
                            it.registration,
                            it.year
                        )
                    } ?: null
                }

                override fun onFailure(call: Call<AddCarReponse>, t: Throwable) {
                    lvdResponseError.value = AppError(t.localizedMessage)
                }
            })
        } else {
            Executors.newSingleThreadExecutor().execute {
                val retCar = mCar.getDataObj().apply { synced = false }
                mAppDatabase.carDao().insertCar(retCar)
                 if(!testMode)
                 Handler(Looper.getMainLooper()).post {
                        lvdCarObj.value = mCar.apply { synced = false }
                 }
            }
        }
    }

    private fun updateWith(m: co.mvvm_dagger_iteo.data.models.Car): Boolean {
        if (m?._id.isNullOrEmpty()) return true
        val cars = mAppDatabase.carDao().getCarByID(m._id!!)
        cars.firstOrNull()?.let {
            mAppDatabase.carDao().updateCar(it.apply {
                brand = m.brand
                model = m.model
                color = m.color
                year = m.year
                lat = m.lat
                lng = m.lng
                ownerId = m.ownerId
                registration = m.registration
                synced = true
            })
            return true
        } ?: return false
    }
}