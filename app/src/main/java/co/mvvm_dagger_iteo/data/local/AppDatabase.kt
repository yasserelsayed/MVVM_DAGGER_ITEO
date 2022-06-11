package co.mvvm_dagger_iteo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import co.mvvm_dagger_iteo.data.models.Car
import co.mvvm_dagger_iteo.data.models.Person

@Database(entities = [Car::class,Person::class],version=1,exportSchema = false)
abstract class AppDatabase :RoomDatabase(){
     abstract fun carDao():CarDao
     abstract fun personDao():PersonDao
}