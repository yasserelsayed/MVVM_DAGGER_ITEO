package co.mvvm_dagger_iteo.di.modules

import androidx.lifecycle.ViewModel
import co.mvvm_dagger_iteo.data.CarsRepository
import co.mvvm_dagger_iteo.data.PersonsRepository
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.remote.CarService
import co.mvvm_dagger_iteo.data.remote.PersonService
import co.mvvm_dagger_iteo.di.scope.ViewScope
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
import co.mvvm_dagger_iteo.ui.cars.viewModels.PersonsViewModel
import dagger.Module
import dagger.Provides

@Module
@ViewScope
class ViewModelFactoryModule {
    @Provides
    fun provideCarsViewModelFactory(mCarService: CarService,mAppDatabase: AppDatabase, mApp: App,mAppSession: AppSession): ViewModelProviders.CarsViewModelFactory {
        return object : ViewModelProviders.CarsViewModelFactory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CarsViewModel(CarsRepository(mCarService,mAppDatabase,mApp,mAppSession),mApp) as T
            }
        }
    }

    @Provides
    fun providePersonsViewModelFactory(mPersonService: PersonService,mAppDatabase: AppDatabase, mApp: App,mAppSession: AppSession): ViewModelProviders.PersonsViewModelFactory {
        return object : ViewModelProviders.PersonsViewModelFactory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PersonsViewModel(PersonsRepository(mPersonService,mAppDatabase,mApp,mAppSession)) as T
            }
        }
    }

}