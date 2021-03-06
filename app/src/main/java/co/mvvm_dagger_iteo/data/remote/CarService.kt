package co.mvvm_dagger_iteo.data.remote

import co.mvvm_dagger_iteo.data.models.AddCarReponse
import co.mvvm_dagger_iteo.data.models.Car
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CarService {
    @GET("car-list")
    fun getCars(): Call<List<Car>>
    @POST("car-list")
    fun addCar(@Body m:Car): Call<AddCarReponse>
}

