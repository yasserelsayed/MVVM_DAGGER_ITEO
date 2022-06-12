package co.mvvm_dagger_iteo.data

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.remote.PersonService
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.domain.AppError
import co.mvvm_dagger_iteo.domain.Person
import retrofit2.Call
import retrofit2.Callback
import java.util.concurrent.Executors
import javax.inject.Inject

class PersonsRepository @Inject constructor(private val mPersonService: PersonService,private val mAppDatabase: AppDatabase, private val mApp: App, val mAppSession: AppSession) {
    val lvdResponseError = MutableLiveData<AppError>()
    val lvdlstPersons= MutableLiveData<List<Person>>()
    var testMode = false
    fun getPersons(){
        if(mApp.isNetworkAvailable()) {
            mPersonService.getPersons().enqueue(object : Callback<List<co.mvvm_dagger_iteo.data.models.Person>> {
                override fun onResponse(call: Call<List<co.mvvm_dagger_iteo.data.models.Person>>, response: retrofit2.Response<List<co.mvvm_dagger_iteo.data.models.Person>>) {
                    Executors.newSingleThreadExecutor().execute {
                        val persons = response.body()
                        persons?.let {
                            it.forEach { person ->
                                if (!updateWith(person))
                                    mAppDatabase.personDao().insertPerson(person)
                            }
                        }
                        val lstPersons  = mAppDatabase.personDao().gelAllPersons()?.map { Person(it) }
                        if(testMode)   lvdlstPersons.value = lstPersons
                        else
                        Handler(Looper.getMainLooper()).post {
                            lvdlstPersons.value = lstPersons
                        }
                    }
                }
                override fun onFailure(call: Call<List<co.mvvm_dagger_iteo.data.models.Person>>, t: Throwable) {
                    lvdResponseError.value = AppError(t.localizedMessage)
                }
            })
        } else {
            Executors.newSingleThreadExecutor().execute {
                val lstPersons = mAppDatabase.personDao().gelAllPersons().map { Person(it) }
                if(testMode)   lvdlstPersons.value = lstPersons
                else Handler(Looper.getMainLooper()).post {
                        lvdlstPersons.value = lstPersons
                    }
            }
        }
    }


    fun getCachedPersonsData(){
        Executors.newSingleThreadExecutor().execute {
            val lstPersons  = mAppDatabase.personDao().gelAllPersons()?.map { Person(it) }
            if(testMode)   lvdlstPersons.value = lstPersons
            else
            Handler(Looper.getMainLooper()).post {
                lvdlstPersons.value = lstPersons
            }
        }
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