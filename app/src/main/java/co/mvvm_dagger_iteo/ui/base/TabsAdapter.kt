package co.mvvm_dagger_iteo.ui.base


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import co.mvvm_dagger_iteo.ui.base.TabScreen

class TabsAdapter(val fm:FragmentManager ,val lst:List<Fragment>): FragmentStatePagerAdapter(fm) {

  override  fun getItem(position: Int): Fragment {
        return lst!![position]
    }


    override fun getCount(): Int {
        return lst!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
       val mTabScreen = lst!![position] as TabScreen
        return mTabScreen.getTabTitle()
    }
}