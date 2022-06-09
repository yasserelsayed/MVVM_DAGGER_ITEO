package co.mvvm_dagger_iteo.data.network

import co.mvvm_dagger_iteo.data.models.Person
import retrofit2.Call
import retrofit2.http.GET

interface PersonService {
    @GET("person-list")
    fun getPersons(): Call<List<Person>>
}

