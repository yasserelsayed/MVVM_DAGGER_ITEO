package co.mvvm_dagger_iteo.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.mvvm_dagger_iteo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}