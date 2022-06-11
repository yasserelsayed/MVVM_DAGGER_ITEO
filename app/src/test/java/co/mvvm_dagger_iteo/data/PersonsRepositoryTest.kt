package co.mvvm_dagger_iteo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.remote.PersonService
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


class PersonsRepositoryTest {

    private lateinit var mPersonService: PersonService
    private val mAppDatabase: AppDatabase = Mockito.mock(AppDatabase::class.java)
    private val mApp: App =  Mockito.mock(App::class.java)
    private val mAppSession: AppSession = Mockito.mock(AppSession::class.java)
    private lateinit var mPersonsRepository: PersonsRepository
    private lateinit var mMockWebServer:MockWebServer
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setUp(){
       val client =  OkHttpClient.Builder()
               .connectTimeout(10000, TimeUnit.SECONDS)
               .readTimeout(10000, TimeUnit.SECONDS)
               .writeTimeout(10000, TimeUnit.SECONDS).build()
        mMockWebServer =   MockWebServer()
        mPersonService =  (Retrofit.Builder()
                .baseUrl(mMockWebServer.url("/"))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()).create(PersonService::class.java)

        mPersonsRepository = PersonsRepository(mPersonService,mAppDatabase,mApp,mAppSession)

//        Mockito.`when`(mPersonService.getPersons()).then { object:Call<List<Person>>{
//            override fun clone(): Call<List<Person>> {
//                return this
//            }
//            override fun execute(): Response<List<Person>> {
//                return Response.success(200, listOf())
//            }
//
//            override fun enqueue(callback: Callback<List<Person>>) {
//                val i = 9+3
//            }
//
//            override fun isExecuted(): Boolean {
//                return false
//            }
//
//            override fun cancel() {
//
//            }
//
//            override fun isCanceled(): Boolean {
//                return false
//            }
//
//            override fun request(): Request {
//                return Request.Builder().build()
//            }
//
//            override fun timeout(): Timeout {
//                return Timeout()
//            }
//
//        }.execute() }

      Mockito.`when`(mApp.isNetworkAvailable()).thenReturn(true)
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
      //  mMockWebServer.shutdown()
    }



}