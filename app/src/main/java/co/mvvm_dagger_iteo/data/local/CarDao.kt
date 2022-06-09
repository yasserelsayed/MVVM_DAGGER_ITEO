package co.mvvm_dagger_iteo.data.local

import androidx.room.*
import co.mvvm_dagger_iteo.data.models.Car

@Dao
interface CarDao {
    @Insert
    fun insertCar(m: Car)

    @Query("Select * from car")
    fun gelAllCars(): List<Car>

    @Update
    fun updateCar(m: Car)

    @Delete
    fun deleteCar(m: Car)
}