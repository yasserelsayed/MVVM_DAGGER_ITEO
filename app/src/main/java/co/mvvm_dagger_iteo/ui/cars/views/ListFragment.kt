package co.mvvm_dagger_iteo.ui.cars.views

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.databinding.FragmentCarsBinding
import co.mvvm_dagger_iteo.databinding.FragmentListBinding
import co.mvvm_dagger_iteo.domain.Car
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabScreen
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
import javax.inject.Inject


class ListFragment(val lstCars:List<Car>,val screenTitle:String) : AppFragment(),TabScreen {
    private var _Binding: FragmentListBinding? = null
    private lateinit var binding: FragmentListBinding
    @Inject
    lateinit var mCarsViewModelFactory: ViewModelProviders.CarsViewModelFactory
    lateinit var mCarsViewModel: CarsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDaggerViewComponent.inject(this)
        mCarsViewModel  = ViewModelProvider(this, mCarsViewModelFactory).get(CarsViewModel::class.java)
        observeViewError(mCarsViewModel.lvdResponseError)
        _Binding = FragmentListBinding.inflate(inflater,container, false)
        binding = _Binding!!
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if(lstCars.isEmpty()) binding.tagNoData.visibility = VISIBLE
        else binding.tagNoData.visibility = GONE
        val mCarsRecycler =  CarsRecycler(lstCars){mCar,fromParent->
            if(fromParent)
                initialPopup(CarDetailsFragment(mCar))
            else{
                val ret =  mCarsViewModel.syncWithServer(mCar)
                if(ret==-1) {
                    showLoading()
                    mCar.synced = true
                } else Toast.makeText(mMainActivity,getString(ret),Toast.LENGTH_LONG).show()
            }
        }
        binding.rclCarsItems.adapter = mCarsRecycler
        binding.rclCarsItems.layoutManager = LinearLayoutManager(mMainActivity)
        mCarsViewModel.lvdCarObj.observe(viewLifecycleOwner){
            hideLoading()
            mCarsRecycler.notifyDataSetChanged()
            Toast.makeText(mMainActivity,getString(R.string.msg_car_submitted_successfully),Toast.LENGTH_LONG).show()
        }
    }

    override fun getTabTitle(): String {
      return  screenTitle
    }

    override fun onScreenLoad() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

}