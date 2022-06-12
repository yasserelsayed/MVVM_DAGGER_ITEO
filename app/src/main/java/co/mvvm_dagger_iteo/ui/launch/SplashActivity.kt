package co.mvvm_dagger_iteo.ui.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.di.components.DaggerViewComponent
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.base.MainActivity
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
import co.mvvm_dagger_iteo.ui.cars.viewModels.PersonsViewModel
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var mPersonsViewModelFactory: ViewModelProviders.PersonsViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val mDaggerViewComponent  =  DaggerViewComponent.builder()
            .appComponent((application as App).mAppComponent)
            .build()
        mDaggerViewComponent.inject(this)
        val mPersonsViewModel  = ViewModelProvider(this, mPersonsViewModelFactory).get(PersonsViewModel::class.java)
        mPersonsViewModel.getPersons()
        mPersonsViewModel.lvdResponseError.observe(this,{
            Toast.makeText(this,it.errorMessage,Toast.LENGTH_LONG).show()
        })
        mPersonsViewModel.lvdlstPersons.observe(this,{
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
            },2000)
        })


    }
}