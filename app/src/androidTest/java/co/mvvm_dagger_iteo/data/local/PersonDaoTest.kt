package co.mvvm_dagger_iteo.data.local

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import co.mvvm_dagger_iteo.data.models.Person
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test

class PersonDaoTest {

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

    // id = 8984
    @Test
    fun testUpdateWith_ReturnFalse(){
        val person = getPerson()
        val ret = mAppDatabase.personDao().updateWith(person)
        Truth.assertThat(ret).isEqualTo(false)
    }

    // id = 8984
    @Test
    fun testUpdateWith_ReturnTrue(){
        val person = getPerson()
        mAppDatabase.personDao().insertPerson(person)
        val ret = mAppDatabase.personDao().updateWith(person)
        Truth.assertThat(ret).isEqualTo(true)
    }

    // id = null
    @Test
    fun testUpdateWith_NoId(){
        val person = getPerson()
        val ret = mAppDatabase.personDao().updateWith(person.apply { _id = null })
        Truth.assertThat(ret).isEqualTo(true)
    }


}