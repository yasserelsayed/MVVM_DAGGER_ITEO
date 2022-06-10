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

    fun updateWith(m: Person):Boolean{
        if(m._id.isNullOrEmpty()) return true
        val persons = getPersonByID(m._id!!)
        persons.firstOrNull()?.let{
            updatePerson(it.apply {
                birth_date = it.birth_date
                first_name = it.first_name
                last_name = it.last_name
                sex = it.sex
            } )
            return  true
        }?:return false
    }


}