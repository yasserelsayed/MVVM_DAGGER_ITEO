package co.mvvm_dagger_iteo.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.mvvm_dagger_iteo.R
import kotlinx.android.synthetic.main.custom_loading.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showLoading() {
        rlt_loader?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        rlt_loader?.visibility = View.GONE
    }
}