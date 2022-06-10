package co.mvvm_dagger_iteo.data.local

import androidx.room.*
import co.mvvm_dagger_iteo.data.models.Car

@Dao
interface CarDao {
    @Insert
    fun insertCar(m: Car)

    @Query("Select * from car")
    fun gelAllCars(): List<Car>


    @Query("Select * from car WHERE synced = 0")
    fun getUnSyncedCars(): List<Car>

    @Query("Select * from car WHERE _id = :id")
    fun getCarByID(id:String): List<Car>

    @Update
    fun updateCar(m: Car)

    fun updateWith(m: Car):Boolean{
      if(m._id.isNullOrEmpty()) return true
       val cars = getCarByID(m._id!!)
        cars.firstOrNull()?.let {
           updateCar( it.apply {
                brand = m.brand
                color = m.color
                year = m.year
                lat = m.lat
                lng = m.lng
                ownerId = m.ownerId
                registration = m.registration
            })
            return  true
        }?:return false
    }

    @Delete
    fun deleteCar(m: Car)
}