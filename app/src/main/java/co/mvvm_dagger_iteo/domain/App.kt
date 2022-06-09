package co.mvvm_dagger_iteo.domain

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager




class App :Application() {

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

}