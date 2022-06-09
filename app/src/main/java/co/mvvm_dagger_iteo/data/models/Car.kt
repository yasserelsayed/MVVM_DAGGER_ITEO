package co.mvvm_dagger_iteo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Car(
    @PrimaryKey
    val id:Int?,
    val _id: String?,
    val brand: String,
    val color: String,
    val lat: Double,
    val lng: Double,
    val model: String,
    val ownerId: String,
    val registration: String,
    val year: String
)