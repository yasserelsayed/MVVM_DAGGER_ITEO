package co.mvvm_dagger_iteo.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import co.mvvm_dagger_iteo.data.models.Car
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CarDaoTest {
    lateinit var mAppDatabase:AppDatabase
    @Before
    fun setUp(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        mAppDatabase = Room.inMemoryDatabaseBuilder(context,AppDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    fun setDown(){
        mAppDatabase.close()
    }

    fun getCar() = Car("bm","red",0.1,0.2,"model 5","65856785","register","1989","8787",1)

    @Test
    fun testInsert(){
        mAppDatabase.carDao().insertCar(getCar())
        val count = mAppDatabase.carDao().gelAllCars().size
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun getUnSyncedCarsTest(){
        mAppDatabase.carDao().insertCar(getCar())
        val count = mAppDatabase.carDao().getUnSyncedCars().size
        assertThat(count).isEqualTo(0)
    }


    @Test
    fun getCarByIDTest(){
        mAppDatabase.carDao().insertCar(getCar())
        val count = mAppDatabase.carDao().getCarByID("8787").size
        assertThat(count).isEqualTo(1)
    }



    @Test
    fun testDelete(){
        val car = getCar()
        mAppDatabase.carDao().insertCar(car)
        val ret = mAppDatabase.carDao().deleteCar(car)
        val count = mAppDatabase.carDao().gelAllCars().size
        assertThat(count).isEqualTo(0)
    }


    @Test
    fun testUpdate(){
         val car = getCar()
         mAppDatabase.carDao().insertCar(car)
         car.synced = false
         mAppDatabase.carDao().updateCar(car)
         val target = mAppDatabase.carDao().gelAllCars()[0]
         assertThat(target.synced).isEqualTo(false)
    }

    // id = 8787
    @Test
    fun testUpdateWith_ReturnFalse(){
        val car = getCar()
        val ret = mAppDatabase.carDao().updateWith(car)
        assertThat(ret).isEqualTo(false)
    }

    // id = 8787
    @Test
    fun testUpdateWith_ReturnTrue(){
        val car = getCar()
        mAppDatabase.carDao().insertCar(car)
        val ret = mAppDatabase.carDao().updateWith(car)
        assertThat(ret).isEqualTo(true)
    }

    // id = null
    @Test
    fun testUpdateWith_NoId(){
        val car = getCar()
        val ret = mAppDatabase.carDao().updateWith(car.apply { _id = null })
        assertThat(ret).isEqualTo(true)
    }

}