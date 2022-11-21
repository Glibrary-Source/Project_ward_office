package com.example.tastywardoffice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.tastywardoffice.databinding.FragmentGoogleMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class google_map : Fragment(), OnMapReadyCallback {

    private val TAG = "MapFragment"
    lateinit var mContext: Context
    private lateinit var mView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latiTude = 37.56
    private var longItude = 126.97
    lateinit var geocoder : Geocoder


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }

        geocoder = Geocoder(mContext)

        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentGoogleMapBinding.inflate(inflater)
        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        checkLocationPermission()

        Log.d(TAG, "onCreateView")

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        mView.onStart()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        mView.onResume()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val current = LatLng(latiTude, longItude)
        googleMap.addMarker(MarkerOptions().position(current).title("현재").snippet("위치"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(current))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13f))

        Log.d(TAG, "onMapReady")
    }

    //    퍼미션 체크 및 권한 요청 함수
    @SuppressLint("MissingPermission")
    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) { fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    geocoder = Geocoder(mContext, Locale.KOREA)
                    val adress = geocoder.getFromLocation(latiTude, longItude, 1)
                    if (location != null) {
                        Toast.makeText(
                            mContext,
                            "주소 : ${adress[0].subLocality}",
                            Toast.LENGTH_SHORT
                        ).show()
                        latiTude = location.latitude
                        longItude = location.longitude
                    }
                }
        } else {
            Toast.makeText(mContext, "위치권한이 없습니다..", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        mView.onStop()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        mView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d(TAG, "onLowMemory")
        mView.onLowMemory()
    }

    override fun onDestroy() {
        mView.onDestroy()
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

}