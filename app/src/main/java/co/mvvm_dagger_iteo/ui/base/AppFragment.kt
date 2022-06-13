package co.mvvm_dagger_iteo.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import co.mvvm_dagger_iteo.di.components.DaggerViewComponent
import co.mvvm_dagger_iteo.di.components.ViewComponent
import co.mvvm_dagger_iteo.domain.App
import co.mvvm_dagger_iteo.domain.AppError

open class AppFragment: Fragment() {
    protected lateinit var mMainActivity: MainActivity
    protected lateinit var mApp: App
    protected lateinit var mDaggerViewComponent: ViewComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainActivity = activity as MainActivity
        mApp = mMainActivity?.application as App
        mDaggerViewComponent  =  DaggerViewComponent.builder()
            .appComponent(mApp.mAppComponent)
            .build()
    }

    fun showLoading() {
        mMainActivity.showLoading()
    }

    fun hideLoading() {
        mMainActivity.hideLoading()
    }

    fun initialPopup(mfragment: Fragment) {
       mMainActivity.initialPopup(mfragment)
    }

    fun closePopup() {
        mMainActivity.closePopup()
    }

    fun observeViewError( observable: LiveData<AppError>){
        observable.observe(viewLifecycleOwner,{
            mMainActivity.showErrorDialog(it.errorMessage)
            hideLoading()
        })
    }

}