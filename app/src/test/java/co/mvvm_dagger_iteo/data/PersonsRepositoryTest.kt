package co.mvvm_dagger_iteo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.PersonDao
import co.mvvm_dagger_iteo.data.models.Person
import co.mvvm_dagger_iteo.di.components.DaggerTestAppComponent
import co.mvvm_dagger_iteo.di.moduls.TestAppModule
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.util.Constants
import co.mvvm_dagger_iteo.util.getOrAwaitValue
import com.google.common.reflect.TypeToken
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.kotlin.any
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class PersonsRepositoryTest {

    @Inject
    lateinit var mAppDatabase: AppDatabase
    @Inject
    lateinit var mApp: App
    @Inject
    lateinit var mPersonsRepository: PersonsRepository
    @Inject
    lateinit var mMockWebServer:MockWebServer
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var datasource = mutableListOf<Person>()

    @Before
    fun setUp(){
       val mDaggerTestAppComponent = DaggerTestAppComponent.builder().testAppModule(TestAppModule()).build()
        mDaggerTestAppComponent.inject(this)
        Mockito.`when`(mApp.isNetworkAvailable()).thenReturn(true)
        Mockito.`when`(mAppDatabase.personDao()).thenReturn(Mockito.mock(PersonDao::class.java))
        Mockito.`when`(mAppDatabase.personDao().gelAllPersons()).thenAnswer { datasource.toList() }
        Mockito.`when`(mAppDatabase.personDao().insertPerson(any<Person>())).thenAnswer{
           val p= it.arguments[0] as Person
            datasource.add(p)
        }

        Mockito.`when`(mAppDatabase.personDao().getPersonByID(anyString())).thenAnswer {
            val id = it.arguments[0]
            datasource.filter { it._id ==  id}
        }

        Mockito.`when`(mAppDatabase.personDao().updatePerson(any<Person>())).thenAnswer {
            val person = it.arguments[0] as Person
            val current = datasource.firstOrNull { it._id ==  person._id}
            current?.apply {
                birth_date = person.birth_date
                first_name = person.first_name
                last_name = person.last_name
                sex = person.sex
            }
        }
        mPersonsRepository.testMode = true
    }

    @Test
    fun `online get owners list test case`(){
        val mMockResponse = MockResponse()
        mMockResponse.setResponseCode(200)
        mMockResponse.setBody(Constants.PERSONS)
        mMockWebServer.enqueue(mMockResponse)
        mPersonsRepository.getPersons()
        val persons:List<co.mvvm_dagger_iteo.domain.Person> = mPersonsRepository.lvdlstPersons.getOrAwaitValue()
        assertThat(persons.size).isEqualTo(1)
    }

    @Test
    fun `offline get owners list test case`(){
        Mockito.`when`(mApp.isNetworkAvailable()).thenReturn(false)
        mPersonsRepository.getPersons()
        val persons:List<co.mvvm_dagger_iteo.domain.Person> = mPersonsRepository.lvdlstPersons.getOrAwaitValue()
        assertThat(persons.size).isEqualTo(0)
    }

    @Test
    fun `update owners local storage with live data case`(){
        val typeToken = object : TypeToken<MutableList<Person>>() {}.type
        datasource =  Gson().fromJson(Constants.PERSONS, typeToken)
        val mMockResponse = MockResponse()
        mMockResponse.setResponseCode(200)
        mMockResponse.setBody(Constants.PERSONSUPDATED)
        mMockWebServer.enqueue(mMockResponse)
        mPersonsRepository.getPersons()
        val persons:List<co.mvvm_dagger_iteo.domain.Person> = mPersonsRepository.lvdlstPersons.getOrAwaitValue()
        assertThat(persons[0].last_name).containsMatch("UPDATED")
        assertThat(persons.size).isEqualTo(2)
    }

    @After
    fun setDown(){
          mMockWebServer.shutdown()
    }



}

