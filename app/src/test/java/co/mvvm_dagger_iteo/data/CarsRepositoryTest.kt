package co.mvvm_dagger_iteo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.CarDao
import co.mvvm_dagger_iteo.data.models.Car
import co.mvvm_dagger_iteo.domain.Person
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


class CarsRepositoryTest {

    @Inject
    lateinit var mAppDatabase: AppDatabase
    @Inject
    lateinit var mApp: App
    @Inject
    lateinit var mCarsRepository: CarsRepository
    @Inject
    lateinit var mMockWebServer:MockWebServer
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private var datasource = mutableListOf<Car>()
    val typeToken = object : TypeToken<MutableList<Person>>() {}.type

    @Before
    fun setUp(){
       val mDaggerTestAppComponent = DaggerTestAppComponent.builder().testAppModule(TestAppModule()).build()
        mDaggerTestAppComponent.inject(this)
        Mockito.`when`(mApp.isNetworkAvailable()).thenReturn(true)
        Mockito.`when`(mAppDatabase.carDao()).thenReturn(Mockito.mock(CarDao::class.java))
        Mockito.`when`(mAppDatabase.carDao().gelAllCars()).thenAnswer { datasource.toList() }
        Mockito.`when`(mAppDatabase.carDao().insertCar(any<Car>())).thenAnswer{
           val c= it.arguments[0] as Car
            datasource.add(c)
        }
        Mockito.`when`(mCarsRepository.mPersonsRepository.getCachedPersonsData()).thenAnswer {
            Gson().fromJson(Constants.PERSONS, typeToken)
        }


        Mockito.`when`(mAppDatabase.carDao().getCarByID(anyString())).thenAnswer {
            val id = it.arguments[0]
            datasource.filter { it._id ==  id}
        }

        Mockito.`when`(mAppDatabase.carDao().updateCar(any<Car>())).thenAnswer {
            val car = it.arguments[0] as Car
            val current = datasource.firstOrNull { it._id ==  car._id}
            current?.apply {
                brand = car.brand
                color = car.color
                year = car.year
                lat = car.lat
                lng = car.lng
                ownerId = car.ownerId
                registration = car.registration
             }

        }
    }

    @Test
    fun `online get car list case`(){
        val mMockResponse = MockResponse()
        mMockResponse.setResponseCode(200)
        mMockResponse.setBody(Constants.CARS)
        mMockWebServer.enqueue(mMockResponse)
        mCarsRepository.getCars()
        val cars:List<co.mvvm_dagger_iteo.domain.Car> = mCarsRepository.lvdlstCars.getOrAwaitValue()
        assertThat(cars.size).isEqualTo(2)
        assertThat(cars[0].ownerFullname).containsMatch("Predovic")
        assertThat(cars[0].synced).isTrue()
    }

    @Test
    fun `offline get car list test case`(){
        Mockito.`when`(mApp.isNetworkAvailable()).thenReturn(false)
        mCarsRepository.getCars()
        val cars:List<co.mvvm_dagger_iteo.domain.Car> = mCarsRepository.lvdlstCars.getOrAwaitValue()
        assertThat(cars.size).isEqualTo(0)
    }

    @Test
    fun `update cars local storage with live data case`(){
        val typeToken = object : TypeToken<MutableList<Car>>() {}.type
        datasource =  Gson().fromJson(Constants.CARS, typeToken)
        val mMockResponse = MockResponse()
        mMockResponse.setResponseCode(200)
        mMockResponse.setBody(Constants.CARSUPDATED)
        mMockWebServer.enqueue(mMockResponse)
        mCarsRepository.getCars()
        val cars:List<co.mvvm_dagger_iteo.domain.Car> = mCarsRepository.lvdlstCars.getOrAwaitValue()
        assertThat(cars[0].model).containsMatch("2")
        assertThat(cars.size).isEqualTo(3)
    }

    @Test
    fun `offline add new car case`(){
        Mockito.`when`(mApp.isNetworkAvailable()).thenReturn(false)
        val mCar = co.mvvm_dagger_iteo.domain.Car("opel","#000",1.0,2.0,"opel 2020","9484","uu","1950")
        mCarsRepository.addCar(mCar)
        val car = mCarsRepository.lvdCarObj.getOrAwaitValue()
        assertThat(car?.synced).isFalse()
    }

    @Test
    fun `validate new car cars`(){
        Mockito.`when`(mApp.isNetworkAvailable()).thenReturn(false)
        val mCar = co.mvvm_dagger_iteo.domain.Car("opel","",1.0,1.0,"","9484","","1950")
        assertThat(mCar?.validateModel().size).isEqualTo(3)
    }


    @After
    fun setDown(){
          mMockWebServer.shutdown()
    }



}

