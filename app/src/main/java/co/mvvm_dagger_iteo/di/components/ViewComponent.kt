package co.mvvm_dagger_iteo.di.components

import co.mvvm_dagger_iteo.di.modules.ViewModelFactoryModule
import co.mvvm_dagger_iteo.di.scope.ViewScope
import co.mvvm_dagger_iteo.ui.cars.views.CarDetailsFragment
import co.mvvm_dagger_iteo.ui.cars.views.CarsFragment
import co.mvvm_dagger_iteo.ui.cars.views.ListFragment
import co.mvvm_dagger_iteo.ui.cars.views.NewCarFragment
import dagger.Component


@ViewScope
@Component(modules = [ViewModelFactoryModule::class],dependencies = [AppComponent::class])
interface ViewComponent {
    fun inject(m: ListFragment)
    fun inject(m: CarsFragment)
    fun inject(m: CarDetailsFragment)
    fun inject(m: NewCarFragment)


}