package co.mvvm_dagger_iteo.di.components

import co.mvvm_dagger_iteo.data.CarsRepositoryTest
import co.mvvm_dagger_iteo.data.PersonsRepositoryTest
import co.mvvm_dagger_iteo.di.moduls.TestAppModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [TestAppModule::class])
interface TestAppComponent{
     fun inject(m: PersonsRepositoryTest)
     fun inject(m: CarsRepositoryTest)

}