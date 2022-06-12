package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import co.mvvm_dagger_iteo.databinding.FragmentCarsListBinding
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabsAdapter
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
import kotlinx.android.synthetic.main.fragment_cars.*
import javax.inject.Inject

class CarsFragment : AppFragment() {
    private var _Binding: FragmentCarsListBinding? = null
    private lateinit var binding: FragmentCarsListBinding
    @Inject
    lateinit var mCarsViewModelFactory: ViewModelProviders.CarsViewModelFactory
    lateinit var mCarsViewModel: CarsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDaggerViewComponent.inject(this)
        _Binding = FragmentCarsListBinding.inflate(inflater,container, false)
        binding = _Binding!!
        mCarsViewModel  = ViewModelProvider(this, mCarsViewModelFactory).get(CarsViewModel::class.java)
        observeViewError(mCarsViewModel.lvdResponseError)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
               // (screens[position] as TabScreen).onScreenLoad()
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
        tab_layout.setupWithViewPager(pager)

        showLoading()
        mCarsViewModel.getCars()
        mCarsViewModel.lvdlstCars.observe(viewLifecycleOwner){
            hideLoading()
            val screens = listOf(ListFragment(it),MapFragment(it))
            pager.adapter = TabsAdapter(childFragmentManager,screens)
            pager.currentItem = 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

}