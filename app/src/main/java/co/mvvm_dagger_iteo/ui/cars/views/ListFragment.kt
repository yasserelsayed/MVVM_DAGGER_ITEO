package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.databinding.FragmentListBinding
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabScreen
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
import javax.inject.Inject

class ListFragment : AppFragment(),TabScreen {
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
        _Binding = FragmentListBinding.inflate(inflater,container, false)
        binding = _Binding!!
        mCarsViewModel  = ViewModelProvider(this, mCarsViewModelFactory).get(CarsViewModel::class.java)
        observeViewError(mCarsViewModel.lvdResponseError)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showLoading()
        mCarsViewModel.getCars()
        mCarsViewModel.lvdlstCars.observe(viewLifecycleOwner){
            hideLoading()
            binding.rclCarsItems.adapter = CarsRecycler(it)
            binding.rclCarsItems.layoutManager = LinearLayoutManager(mMainActivity)
        }
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