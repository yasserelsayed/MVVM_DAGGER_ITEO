package co.mvvm_dagger_iteo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.local.PersonDao
import co.mvvm_dagger_iteo.data.remote.PersonService
import co.mvvm_dagger_iteo.di.components.DaggerTestAppComponent
import co.mvvm_dagger_iteo.di.moduls.TestAppModule
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.util.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    @Before
    fun setUp(){
       val mDaggerTestAppComponent = DaggerTestAppComponent.builder().testAppModule(TestAppModule()).build()
        mDaggerTestAppComponent.inject(this)
        Mockito.`when`(mApp.isNetworkAvailable()).thenReturn(true)
        Mockito.`when`(mAppDatabase.personDao()).thenReturn(Mockito.mock(PersonDao::class.java))
        Mockito.`when`(mAppDatabase.personDao().gelAllPersons()).thenReturn(listOf())

    }

    @Test
    fun getPersonsTest(){
        val mMockResponse = MockResponse()
        mMockResponse.setResponseCode(200)
        mMockResponse.setBody("[]")
        mMockWebServer.enqueue(mMockResponse)
        mPersonsRepository.getPersons()
        val persons:List<co.mvvm_dagger_iteo.domain.Person> = mPersonsRepository.lvdlstPersons.getOrAwaitValue()
        assertThat(persons.isEmpty())
    }


    @After
    fun setDown(){
          mMockWebServer.shutdown()
    }



}

