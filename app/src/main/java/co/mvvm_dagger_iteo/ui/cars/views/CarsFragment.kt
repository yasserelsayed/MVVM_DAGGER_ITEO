package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.databinding.FragmentCarsBinding
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabsAdapter
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_cars.*
import javax.inject.Inject

class CarsFragment : AppFragment() {
    private var _Binding: FragmentCarsBinding? = null
    private lateinit var binding: FragmentCarsBinding
    @Inject
    lateinit var mCarsViewModelFactory: ViewModelProviders.CarsViewModelFactory
    lateinit var mCarsViewModel: CarsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDaggerViewComponent.inject(this)
        _Binding = FragmentCarsBinding.inflate(inflater,container, false)
        binding = _Binding!!
        mCarsViewModel  = ViewModelProvider(this, mCarsViewModelFactory).get(CarsViewModel::class.java)
        observeViewError(mCarsViewModel.lvdResponseError)
        binding.root.findViewById<TabLayout>(R.id.tab_layout).setupWithViewPager(binding.pager)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.fltAddNew.setOnClickListener {
            findNavController().navigate(R.id.action_carsFragment_to_newCarFragment)
        }
        showLoading()
        mCarsViewModel.getCars()
        mCarsViewModel.lvdlstCars.observe(viewLifecycleOwner){
            hideLoading()
            val screens = listOf(ListFragment(it,getString(R.string.tag_list)),MapFragment(it,getString(R.string.tag_list)))
            binding.pager.adapter = TabsAdapter(childFragmentManager,screens)
            binding.pager.currentItem = 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

}