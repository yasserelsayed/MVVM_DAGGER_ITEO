package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.mvvm_dagger_iteo.databinding.FragmentCarDetailsBinding
import co.mvvm_dagger_iteo.domain.Car
import co.mvvm_dagger_iteo.ui.base.AppFragment


class CarDetailsFragment(val mCar:Car) : AppFragment() {
    private var _Binding: FragmentCarDetailsBinding? = null
    private lateinit var binding: FragmentCarDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDaggerViewComponent.inject(this)
        _Binding = FragmentCarDetailsBinding.inflate(inflater,container, false)
        binding = _Binding!!
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            txtBrand.text = mCar.brand
            txtModel.text = mCar.model
            txtRegistration.text = mCar.registration
            txtOwner.text = mCar.ownerFullname
            txtYear.text = mCar.year
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

}