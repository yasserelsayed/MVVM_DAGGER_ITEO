package co.mvvm_dagger_iteo.di.moduls

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import co.mvvm_dagger_iteo.data.CarsRepository
import co.mvvm_dagger_iteo.data.PersonsRepository
import co.mvvm_dagger_iteo.data.local.AppDatabase
import co.mvvm_dagger_iteo.data.local.AppSession
import co.mvvm_dagger_iteo.data.remote.CarService
import co.mvvm_dagger_iteo.data.remote.PersonService
import co.mvvm_dagger_iteo.domain.App
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.mockito.Mockito
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@Singleton
class TestAppModule{

    @Provides
    @Singleton
    fun provideAppDatabase(): AppDatabase = Mockito.mock(AppDatabase::class.java)

    @Provides
    @Singleton
    fun provideAppSession(): AppSession = Mockito.mock(AppSession::class.java)

    @Provides
    @Singleton
    fun provideInstantTaskExecutorRule(): InstantTaskExecutorRule =  InstantTaskExecutorRule()

    @Provides
    @Singleton
    fun provideApp(): App = Mockito.mock(App::class.java)

    @Provides
    @Singleton
    fun provideMockServer(): MockWebServer = MockWebServer()

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(m: OkHttpClient,mMockWebServer:MockWebServer): Retrofit {
        return  Retrofit.Builder()
            .baseUrl(mMockWebServer.url("/"))
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
    fun providePersonsRepository(mPersonService:PersonService,mAppDatabase:AppDatabase,mApp:App,mAppSession:AppSession): PersonsRepository = PersonsRepository(mPersonService,mAppDatabase,mApp,mAppSession)

    @Provides
    @Singleton
    fun provideCarsRepository(mCarService:CarService,mAppDatabase:AppDatabase,mApp:App,mAppSession:AppSession): CarsRepository = CarsRepository(mCarService,Mockito.mock(PersonsRepository::class.java),mAppDatabase,mApp,mAppSession)

}