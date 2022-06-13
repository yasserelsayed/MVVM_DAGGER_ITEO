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
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapPickerFragment(val callback:(latLng: LatLng)->Unit) : AppFragment(),OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mSupportMapFragment: SupportMapFragment
    private lateinit var  mLocationManager: LocationManager
    private val LOCATION_REFRESH_TIME:Long = 15000 // 15 seconds to update

    private val LOCATION_REFRESH_DISTANCE:Float = 500.0f // 500 meters to update
    private var mCurrentLocationMarker: Marker?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vw = inflater.inflate(R.layout.fragment_map_picker, container, false)
        mSupportMapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)
        mLocationManager =  mMainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return vw
    }



    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        val mLatLng = LatLng(Constants.DEFAULTLATITUDE, Constants.DEFAULTLONGITUDE)
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(mLatLng))
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 9f))
        p0.setMapStyle(MapStyleOptions.loadRawResourceStyle(mMainActivity, R.raw.map_style))

        mGoogleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                callback(p0.position)
            }

            override fun onMarkerDragStart(p0: Marker) {

            }

        })
        requestCurrentLocation()
    }

    private fun requestCurrentLocation(){
        mCurrentLocationMarker = mGoogleMap.addMarker(
            MarkerOptions()
                .position(LatLng(Constants.DEFAULTLATITUDE,Constants.DEFAULTLONGITUDE))
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)))
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
                return
       else {

            try {
                mLocationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE
                ) { location ->
                    val pos = LatLng(location.latitude, location.longitude)
                    mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(pos))
                    mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 7f))
                    if (mCurrentLocationMarker == null)
                        mCurrentLocationMarker = mGoogleMap.addMarker(
                            MarkerOptions()
                                .position(pos)
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                        )
                    else mCurrentLocationMarker?.position = pos
                }
            } catch (ex: SecurityException) {
            }
        }

        callback(mCurrentLocationMarker?.position?: LatLng(Constants.DEFAULTLATITUDE,Constants.DEFAULTLONGITUDE))
    }


}