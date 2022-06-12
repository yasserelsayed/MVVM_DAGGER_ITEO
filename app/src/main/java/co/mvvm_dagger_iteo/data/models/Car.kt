package co.mvvm_dagger_iteo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Car(
    var brand: String,
    var color: String,
    var lat: Double,
    var lng: Double,
    var model: String,
    var ownerId: String,
    var registration: String,
    var year: String,
    var _id: String?=null,
    @PrimaryKey
    val id:Int?=null,
    var synced:Boolean=false
)