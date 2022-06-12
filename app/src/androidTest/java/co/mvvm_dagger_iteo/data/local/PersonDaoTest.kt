package co.mvvm_dagger_iteo.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import co.mvvm_dagger_iteo.data.models.Person
import co.mvvm_dagger_iteo.di.components.DaggerTestAppComponent
import co.mvvm_dagger_iteo.di.moduls.TestAppModule
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class PersonDaoTest {

    lateinit var mAppDatabase:AppDatabase
    lateinit var mContext:Context
    @Before
    fun setUp(){
        mAppDatabase = Room.inMemoryDatabaseBuilder(mContext,AppDatabase::class.java).allowMainThreadQueries().build()
        mContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun setDown(){
        mAppDatabase.close()
    }

    fun getPerson() = Person(1,"8984","25/4","first","second","male")

    @Test
    fun testInsert(){
        mAppDatabase.personDao().insertPerson(getPerson())
        val count = mAppDatabase.personDao().gelAllPersons().size
        Truth.assertThat(count).isEqualTo(1)
    }

    // id = 8984
    @Test
    fun getPersonByIDTest(){
        mAppDatabase.personDao().insertPerson(getPerson())
        val count = mAppDatabase.personDao().getPersonByID("8984").size
        Truth.assertThat(count).isEqualTo(1)
    }

    @Test
    fun testUpdate(){
        val person = getPerson()
        mAppDatabase.personDao().insertPerson(person)
        mAppDatabase.personDao().updatePerson(person.apply { sex = "f" })
        val target = mAppDatabase.personDao().gelAllPersons()[0]
        Truth.assertThat(target.sex).isEqualTo("f")
    }


}