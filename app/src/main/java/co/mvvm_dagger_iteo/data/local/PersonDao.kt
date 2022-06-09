package co.mvvm_dagger_iteo.data.local

import androidx.room.*
import co.mvvm_dagger_iteo.data.models.Car
import co.mvvm_dagger_iteo.data.models.Person

@Dao
interface PersonDao {
    @Query("Select * from person")
    fun gelAllPersons(): List<Person>

}