package co.mvvm_dagger_iteo.data.local

import androidx.room.*
import co.mvvm_dagger_iteo.data.models.Car
import co.mvvm_dagger_iteo.data.models.Person

@Dao
interface PersonDao {

    @Insert
    fun insertPerson(m: Person)

    @Query("Select * from person")
    fun gelAllPersons(): List<Person>

    @Update
    fun updatePerson(m: Person)

    @Query("Select * from person WHERE _id = :id")
    fun getPersonByID(id:String): List<Person>

}