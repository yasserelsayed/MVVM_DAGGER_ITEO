package co.mvvm_dagger_iteo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
    @PrimaryKey
    val id:Int?,
    val _id: String,
    val birth_date: String,
    val first_name: String,
    val last_name: String,
    val sex: String
)