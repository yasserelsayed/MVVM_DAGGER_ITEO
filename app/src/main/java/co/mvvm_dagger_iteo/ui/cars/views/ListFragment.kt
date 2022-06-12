package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.databinding.FragmentListBinding
import co.mvvm_dagger_iteo.domain.Car
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabScreen


class ListFragment(val lstCars:List<Car>) : AppFragment(),TabScreen {
    private var _Binding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDaggerViewComponent.inject(this)
        _Binding = FragmentListBinding.inflate(inflater,container, false)
        binding = _Binding!!
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.rclCarsItems.adapter = CarsRecycler(lstCars)
        binding.rclCarsItems.layoutManager = LinearLayoutManager(mMainActivity)
    }

    override fun getTabTitle(): String {
      return  getString(R.string.txt_list)
    }

    override fun onScreenLoad() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

}