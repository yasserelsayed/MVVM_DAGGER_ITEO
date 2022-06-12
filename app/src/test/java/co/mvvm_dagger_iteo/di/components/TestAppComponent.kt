package co.mvvm_dagger_iteo.di.components

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.mvvm_dagger_iteo.data.PersonsRepositoryTest
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.remote.CarService
import co.mvvm_dagger_iteo.data.remote.PersonService
import co.mvvm_dagger_iteo.di.moduls.TestAppModule
import co.mvvm_dagger_iteo.domain.App
import dagger.Component
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton


@Singleton
@Component(modules = [TestAppModule::class])
interface TestAppComponent{
     fun provideAppSession():AppSession
     fun provideCarService(): CarService
     fun providePersonService(): PersonService
     fun provideApp(): App
     fun provideAppDatabase(): AppDatabase
     fun provideMockServer(): MockWebServer
     fun provideInstantTaskExecutorRule(): InstantTaskExecutorRule
     fun inject(m: PersonsRepositoryTest)
}