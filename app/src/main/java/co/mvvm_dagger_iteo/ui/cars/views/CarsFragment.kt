package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabScreen
import co.mvvm_dagger_iteo.ui.base.TabsAdapter
import kotlinx.android.synthetic.main.fragment_cars.*

class CarsFragment : AppFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cars, container, false)
    }

    override fun onResume() {
        super.onResume()
        val screens = listOf(ListFragment(),MapFragment())
        pager.adapter = TabsAdapter(childFragmentManager,screens)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                (screens[position] as TabScreen).onScreenLoad()
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
        tab_layout.setupWithViewPager(pager)
        pager.currentItem = 0
    }

}