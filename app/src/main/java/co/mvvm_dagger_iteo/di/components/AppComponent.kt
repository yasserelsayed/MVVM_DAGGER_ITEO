package co.mvvm_dagger_iteo.di.components

import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.remote.CarService
import co.mvvm_dagger_iteo.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent{
     fun provideAppSession():AppSession
     fun provideCarService(): CarService
}