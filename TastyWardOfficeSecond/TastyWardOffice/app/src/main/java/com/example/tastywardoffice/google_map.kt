package com.example.tastywardoffice

import android.content.Context
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions



class google_map : Fragment(), OnMapReadyCallback {

    private val TAG = "MapFragment"
    lateinit var mContext: Context
    private lateinit var mView: MapView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }

        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_google_map, container, false)
        mView = rootView.findViewById(R.id.mapView)
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        Log.d(TAG, "onCreateView")

        return rootView
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
        val noryangjin = LatLng(37.514630, 126.945358)

        googleMap.addMarker(MarkerOptions().position(noryangjin).title("노량진 입니다"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(noryangjin))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13f))

        Log.d(TAG, "onMapReady")
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