package co.mvvm_dagger_iteo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
    @PrimaryKey
    var id:Int?,
    var _id: String?,
    var birth_date: String,
    var first_name: String,
    var last_name: String,
    var sex: String
)