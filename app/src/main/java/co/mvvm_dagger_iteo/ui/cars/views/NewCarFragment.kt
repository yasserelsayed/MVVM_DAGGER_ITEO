package co.mvvm_dagger_iteo.ui.cars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.databinding.FragmentListBinding
import co.mvvm_dagger_iteo.databinding.FragmentNewCarBinding
import co.mvvm_dagger_iteo.domain.Car
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabsAdapter
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
import co.mvvm_dagger_iteo.util.Constants
import kotlinx.android.synthetic.main.fragment_cars.*
import javax.inject.Inject

class NewCarFragment : AppFragment() ,View.OnClickListener{

    @Inject
    lateinit var mCarsViewModelFactory: ViewModelProviders.CarsViewModelFactory
    lateinit var mCarsViewModel: CarsViewModel
    private var _Binding: FragmentNewCarBinding? = null
    private lateinit var binding: FragmentNewCarBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDaggerViewComponent.inject(this)
        _Binding = FragmentNewCarBinding.inflate(inflater,container, false)
        binding = _Binding!!
        mCarsViewModel  = ViewModelProvider(this, mCarsViewModelFactory).get(CarsViewModel::class.java)
        observeViewError(mCarsViewModel.lvdResponseError)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnSubmit.setOnClickListener(this)
        mCarsViewModel.lvdCarObj.observe(viewLifecycleOwner){
            hideLoading()
            Toast.makeText(mMainActivity,getString(R.string.msg_new_car_added),Toast.LENGTH_LONG).show()
            mMainActivity.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

    override fun onClick(v: View?) {
       when(v?.id){
           binding.btnSubmit.id->{
               binding.apply {
                  val mCar = Car(edtBrand.text.toString(),edtColor.text.toString(),1.0,2.0,edtModel.text.toString(),"",edtRegistration.text.toString(),edtYear.text.toString())
                  val validation =  mCar.validateModel()
                   if(validation.size == 0){
                       showLoading()
                       mCarsViewModel.addCar(mCar)
                   }else{
                       binding.edtBrand.error = null
                       binding.edtModel.error = null
                       binding.edtYear.error = null
                       binding.edtRegistration.error = null
                       when{
                           validation.containsKey(Constants.DATATAGS.year)->{
                               binding.edtYear.error = getString(validation.get(Constants.DATATAGS.year)!!)
                           }
                           validation.containsKey(Constants.DATATAGS.model)->{
                               binding.edtModel.error = getString(validation.get(Constants.DATATAGS.model)!!)
                           }
                           validation.containsKey(Constants.DATATAGS.brand)->{
                               binding.edtBrand.error = getString(validation.get(Constants.DATATAGS.brand)!!)
                           }
                           validation.containsKey(Constants.DATATAGS.registration)->{
                               binding.edtRegistration.error = getString(validation.get(Constants.DATATAGS.registration)!!)
                           }
                           validation.containsKey(Constants.DATATAGS.color)->{
                              // binding.edtBrand.error = getString(validation.get(Constants.DATATAGS.color)!!)
                           }
                           validation.containsKey(Constants.DATATAGS.ownerId)->{
                              //  binding.spnrPersons.error = getString(validation.get(Constants.DATATAGS.ownerId)!!)
                           }


                       }
                   }
               }

           }
       }
    }
}