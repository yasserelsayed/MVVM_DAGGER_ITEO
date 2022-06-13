package co.mvvm_dagger_iteo.ui.cars.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.databinding.FragmentNewCarBinding
import co.mvvm_dagger_iteo.domain.Car
import co.mvvm_dagger_iteo.ui.ViewModelProviders
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.cars.viewModels.CarsViewModel
import co.mvvm_dagger_iteo.ui.cars.viewModels.PersonsViewModel
import co.mvvm_dagger_iteo.util.Constants
import com.google.android.gms.maps.model.LatLng
import dev.sasikanth.colorsheet.ColorSheet
import dev.sasikanth.colorsheet.utils.ColorSheetUtils
import javax.inject.Inject

class NewCarFragment : AppFragment(), View.OnClickListener {

    @Inject
    lateinit var mCarsViewModelFactory: ViewModelProviders.CarsViewModelFactory
    @Inject
    lateinit var mPersonsViewModelFactory: ViewModelProviders.PersonsViewModelFactory
    lateinit var mCarsViewModel: CarsViewModel
    lateinit var mPersonsViewModel: PersonsViewModel
    private var _Binding: FragmentNewCarBinding? = null
    private lateinit var binding: FragmentNewCarBinding
    private var mCurrentLatLng:LatLng? =null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDaggerViewComponent.inject(this)
        _Binding = FragmentNewCarBinding.inflate(inflater, container, false)
        binding = _Binding!!
        mCarsViewModel = ViewModelProvider(this, mCarsViewModelFactory).get(CarsViewModel::class.java)
        observeViewError(mCarsViewModel.lvdResponseError)
        mPersonsViewModel= ViewModelProvider(this, mPersonsViewModelFactory).get(PersonsViewModel::class.java)
        observeViewError(mPersonsViewModel.lvdResponseError)

        return binding.root
    }



    override fun onStart() {
        super.onStart()

        mPersonsViewModel.getCachedOwners()
        mPersonsViewModel.lvdlstPersons.observe(viewLifecycleOwner){
            var  mArrayAdapter = ArrayAdapter(mMainActivity, R.layout.item_spinner, it.map { it.fullname!!})
            mArrayAdapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spnrPersons.adapter = mArrayAdapter
            binding.spnrPersons.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.spnrPersons.tag = it[position]._id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }

        val colors = resources.getIntArray(R.array.arr_colors)
        binding.txtColor.setOnClickListener {
            ColorSheet().cornerRadius(8).colorPicker(
                colors = colors,
                listener = { color ->
                    binding.txtColor.tag  = ColorSheetUtils.colorToHex(color)
                    binding.txtColor.setBackgroundColor(Color.parseColor( binding.txtColor.tag.toString() ))
                }).show(mMainActivity.supportFragmentManager)
        }
        mCarsViewModel.lvdCarObj.observe(viewLifecycleOwner) {
            hideLoading()
            Toast.makeText(mMainActivity, getString(R.string.msg_new_car_added), Toast.LENGTH_LONG).show()
            mMainActivity.onBackPressed()
        }

        binding.imgBack.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.txtMapPicker.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnSubmit.id -> {
                binding.apply {
                    val mCar = Car(edtBrand.text.toString(), txtColor.tag?.toString()?:"", mCurrentLatLng?.latitude?:-1.0, mCurrentLatLng?.longitude?:-1.0, edtModel.text.toString(), binding.spnrPersons.tag?.toString()?:"", edtRegistration.text.toString(), edtYear.text.toString())
                    val validation = mCar.validateModel()
                    if (validation.size == 0) {
                        showLoading()
                        mCarsViewModel.addCar(mCar)
                    } else {
                        binding.edtBrand.error = null
                        binding.edtModel.error = null
                        binding.edtYear.error = null
                        binding.edtRegistration.error = null
                        binding.txtColor.error = null
                        binding.txtMapPicker.error = null

                        if (validation.containsKey(Constants.DATATAGS.year.name))
                            binding.edtYear.error = getString(validation[Constants.DATATAGS.year.name]!!)
                        if (validation.containsKey(Constants.DATATAGS.model.name))
                            binding.edtModel.error = getString(validation[Constants.DATATAGS.model.name]!!)
                        if (validation.containsKey(Constants.DATATAGS.brand.name))
                            binding.edtBrand.error = getString(validation[Constants.DATATAGS.brand.name]!!)
                        if (validation.containsKey(Constants.DATATAGS.registration.name))
                            binding.edtRegistration.error = getString(validation[Constants.DATATAGS.registration.name]!!)
                        if (validation.containsKey(Constants.DATATAGS.color.name))
                             binding.txtColor.error = getString(validation[Constants.DATATAGS.color.name]!!)
                        if (validation.containsKey(Constants.DATATAGS.ownerId.name))
                             Toast.makeText(mMainActivity,getString(validation[Constants.DATATAGS.ownerId.name]!!),Toast.LENGTH_LONG).show()
                        if (validation.containsKey(Constants.DATATAGS.lat.name))
                            binding.txtMapPicker.error =  getString(validation[Constants.DATATAGS.lat.name]!!)
                    }
                }

            }
            binding.txtMapPicker.id->{
                initialPopup(MapPickerFragment{
                    mCurrentLatLng = it
                    binding.txtMapPicker.setTextColor(ContextCompat.getColor(mMainActivity,R.color.teal_main))
                    binding.txtMapPicker.text = getText(R.string.tag_location_picked)
                })
            }
            binding.imgBack.id->{
                mMainActivity.onBackPressed()
            }
        }
    }

}