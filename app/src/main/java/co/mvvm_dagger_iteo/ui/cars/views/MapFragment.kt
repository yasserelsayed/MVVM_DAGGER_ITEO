package co.mvvm_dagger_iteo.ui.cars.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.domain.Car
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabScreen
import co.mvvm_dagger_iteo.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapFragment(val lstCars:List<Car>) : AppFragment(),TabScreen, OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mSupportMapFragment: SupportMapFragment
    private lateinit var  mLocationManager: LocationManager
    private val LOCATION_REFRESH_TIME:Long = 15000 // 15 seconds to update
    private var lstMarkers: MutableList<Marker> = mutableListOf()
    override fun onStart() {
        super.onStart()
        mGoogleMap?.setOnMarkerClickListener {marker ->
           if(!marker?.snippet.isNullOrEmpty()){
              val selected = lstCars.find { it.id == marker?.snippet }
              selectCar(selected,marker)
           }
            true
        }
    }

    private val LOCATION_REFRESH_DISTANCE:Float = 500.0f // 500 meters to update
    private var mCurrentLocationMarker: Marker?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mSupportMapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)
        mLocationManager =  mMainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requestCurrentLocation()
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun getTabTitle(): String {
        return  getString(R.string.tag_map)
    }

    override fun onScreenLoad() {
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        val mLatLng = LatLng(Constants.DEFAULTLATITUDE, Constants.DEFAULTLONGITUDE)
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(mLatLng))
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 9f))
        p0.setMapStyle(MapStyleOptions.loadRawResourceStyle(mMainActivity, R.raw.map_style))

        bindMapMarkers(lstCars)
    }


   private fun requestCurrentLocation(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return

        try {
            mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE
            ) { location ->
                val pos = LatLng(location.latitude, location.longitude)
                mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(pos))
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 9f))
                if (mCurrentLocationMarker == null)
                    mCurrentLocationMarker = mGoogleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.target))
                    )
                else mCurrentLocationMarker?.position = pos
            }
        } catch(ex: SecurityException) {
        }
    }

    private fun bindMapMarkers(lst:List<Car>){
        mGoogleMap.clear()
        mCurrentLocationMarker?.position?.let {
            mCurrentLocationMarker = mGoogleMap.addMarker(MarkerOptions()
                .position(it)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.target)))
        }
        lst.forEach {
          val marker =  mGoogleMap.addMarker(MarkerOptions()
                .position(LatLng(it.lat, it.lng))
                .title(it.brand)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.carpin))
                .snippet(it.id))
            marker?.let {marker ->  lstMarkers.add(marker) }

        }
        if(!lst.isNullOrEmpty()) {
            val pos = LatLng(lst[0]?.lat, lst[0].lng)
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(pos))
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f))
        }
    }

   private fun selectCar(mCar:Car?,mMarker: Marker?)
    {
        if(mCar ==null) return
        for (item in lstMarkers)
            item.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.carpin))
            mMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
        mMarker?.position?.let {
            mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng( it))
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom( it, 12f))
         }

        initialPopup(CarDetailsFragment(mCar))
    }
}