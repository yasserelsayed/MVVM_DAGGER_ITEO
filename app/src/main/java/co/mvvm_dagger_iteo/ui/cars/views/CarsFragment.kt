package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.databinding.FragmentCarsBinding
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabsAdapter
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
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
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
               // (screens[position] as TabScreen).onScreenLoad()
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
        tab_layout.setupWithViewPager(binding.pager)

        showLoading()
        mCarsViewModel.getCars()
        mCarsViewModel.lvdlstCars.observe(viewLifecycleOwner){
            hideLoading()
            val screens = listOf(ListFragment(it),MapFragment(it))
            binding.pager.adapter = TabsAdapter(childFragmentManager,screens)
            binding.pager.currentItem = 0
        }

        binding.fltAddNew.setOnClickListener {
            findNavController().navigate(R.id.action_carsFragment_to_newCarFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

}