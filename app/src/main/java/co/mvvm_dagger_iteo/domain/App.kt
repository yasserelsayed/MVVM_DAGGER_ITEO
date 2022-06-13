package co.mvvm_dagger_iteo.domain

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.mvvm_dagger_iteo.di.components.AppComponent
import co.mvvm_dagger_iteo.di.components.DaggerAppComponent
import co.mvvm_dagger_iteo.di.modules.AppModule


class App :Application() {

    lateinit var mAppComponent: AppComponent
    var lvdNetworkAvailability:MutableLiveData<Boolean> =MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        mAppComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
       val status = connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
        lvdNetworkAvailability.value = status
        return status
    }

}