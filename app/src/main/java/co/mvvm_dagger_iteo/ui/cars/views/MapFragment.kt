package co.mvvm_dagger_iteo.ui.cars.views

import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.ui.base.AppFragment
import co.mvvm_dagger_iteo.ui.base.TabScreen
import co.mvvm_dagger_iteo.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions

class MapFragment : AppFragment(),TabScreen, OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mSupportMapFragment: SupportMapFragment
    private lateinit var  mLocationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mSupportMapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mSupportMapFragment.getMapAsync(this)
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun getTabTitle(): String {
        return  getString(R.string.txt_map)
    }

    override fun onScreenLoad() {
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        val mLatLng = LatLng(Constants.DEFAULTLATITUDE, Constants.DEFAULTLONGITUDE)
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(mLatLng))
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 9f))
        p0.setMapStyle(MapStyleOptions.loadRawResourceStyle(mMainActivity, R.raw.map_style))
    }
}