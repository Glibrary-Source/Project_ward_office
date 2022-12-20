package com.example.tastywardoffice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tastywardoffice.databinding.FragmentDetailGoogleMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

class detail_googleMap : Fragment(), OnMapReadyCallback {

    private lateinit var mView: MapView
    private val TAG = "MapFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDetailGoogleMapBinding.inflate(inflater)
        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap?) {

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