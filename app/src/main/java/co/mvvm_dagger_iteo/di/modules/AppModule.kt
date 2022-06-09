package co.mvvm_dagger_iteo.di.modules

import android.app.Application
import androidx.room.Room
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.network.CarService
import co.mvvm_dagger_iteo.data.network.PersonService
import co.mvvm_dagger_iteo.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@Singleton
class AppModule(val application: Application){

    @Provides
    @Singleton
    fun provideAppSession(): AppSession {
        return AppSession()
    }

    @Provides
    @Singleton
    fun provideHttpClient(mAppSession: AppSession): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(m: OkHttpClient): Retrofit {
        return  Retrofit.Builder()
            .baseUrl(Constants.ENDPOINT)
            .client(m)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCarService(mRetrofit: Retrofit): CarService {
        return   mRetrofit.create(CarService::class.java)
    }

    @Provides
    @Singleton
    fun providePersonService(mRetrofit: Retrofit): PersonService {
        return   mRetrofit.create(PersonService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase {
        return Room.databaseBuilder(application.applicationContext,
                                   AppDatabase::class.java, "car.db").build()
    }

}