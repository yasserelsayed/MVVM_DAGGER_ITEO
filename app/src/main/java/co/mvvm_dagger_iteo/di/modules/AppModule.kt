package co.mvvm_dagger_iteo.di.modules

import android.app.Application
import androidx.room.Room
import co.mvvm_dagger_iteo.BuildConfig
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.remote.CarService
import co.mvvm_dagger_iteo.data.remote.PersonService
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@Singleton
class AppModule(val application: Application){

    @Provides
    @Singleton
    fun provideAppSession(): AppSession = AppSession()

    @Provides
    @Singleton
    fun provideAppContext(): App = application as App

    @Provides
    @Singleton
    fun provideHttpClient(mAppSession: AppSession): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("x-apikey", BuildConfig.API_KEY)
                chain.proceed(builder.build())
            }).build()
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
    fun provideCarService(mRetrofit: Retrofit): CarService = mRetrofit.create(CarService::class.java)

    @Provides
    @Singleton
    fun providePersonService(mRetrofit: Retrofit): PersonService = mRetrofit.create(PersonService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase = Room.databaseBuilder(application.applicationContext,
                                                                    AppDatabase::class.java, "car.db")
                                                                    .fallbackToDestructiveMigration()
                                                                    .build()

}