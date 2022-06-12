package co.mvvm_dagger_iteo.data

import androidx.lifecycle.MutableLiveData
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.remote.PersonService
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.domain.AppError
import co.mvvm_dagger_iteo.domain.Person
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

class PersonsRepository @Inject constructor(private val mPersonService: PersonService,private val mAppDatabase: AppDatabase, private val mApp: App, val mAppSession: AppSession) {
    val lvdResponseError = MutableLiveData<AppError>()
    val lvdlstPersons= MutableLiveData<List<Person>>()

    fun getPersons(){
        if(mApp.isNetworkAvailable()) {
            mPersonService.getPersons().enqueue(object : Callback<List<co.mvvm_dagger_iteo.data.models.Person>> {
                override fun onResponse(call: Call<List<co.mvvm_dagger_iteo.data.models.Person>>, response: retrofit2.Response<List<co.mvvm_dagger_iteo.data.models.Person>>) {
                   val persons = response.body()
                   persons?.let{
                        it.forEach { person ->
                            if(!updateWith(person))
                                mAppDatabase.personDao().insertPerson(person)
                        }
                  }
                    lvdlstPersons.value = mAppDatabase.personDao().gelAllPersons()?.map { Person(it) }
                }

                override fun onFailure(call: Call<List<co.mvvm_dagger_iteo.data.models.Person>>, t: Throwable) {
                    lvdResponseError.value = AppError(t.localizedMessage)
                }
            })
        } else lvdlstPersons.value  =  mAppDatabase.personDao().gelAllPersons().map { Person(it) }
    }

    fun getCachedPersonsLiveDate(){
        lvdlstPersons.value = mAppDatabase.personDao().gelAllPersons()?.map { Person(it) }
    }

    fun getCachedPersonsData():List<Person>{
        return mAppDatabase.personDao().gelAllPersons()?.map { Person(it) }
    }

    fun updateWith(mPerson: co.mvvm_dagger_iteo.data.models.Person): Boolean {
        if (mPerson._id.isNullOrEmpty()) return true
        val persons = mAppDatabase.personDao().getPersonByID(mPerson._id!!)
        persons.firstOrNull()?.let {
            mAppDatabase.personDao().updatePerson(it.apply {
                birth_date = mPerson.birth_date
                first_name = mPerson.first_name
                last_name = mPerson.last_name
                sex = it.sex
            })
             return true
        } ?: return false
    }

}