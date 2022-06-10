package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.ui.base.AppFragment

class NewCarFragment : AppFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_car, container, false)
    }
}