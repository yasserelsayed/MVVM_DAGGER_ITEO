package co.mvvm_dagger_iteo.ui.base

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.mvvm_dagger_iteo.R
import kotlinx.android.synthetic.main.custom_loading.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 111)
    }

    fun showLoading() {
        rlt_loader?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        rlt_loader?.visibility = View.GONE
    }
}