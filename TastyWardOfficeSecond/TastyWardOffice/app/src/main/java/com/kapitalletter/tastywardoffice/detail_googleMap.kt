package com.kapitalletter.tastywardoffice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kapitalletter.tastywardoffice.databinding.FragmentDetailGoogleMapBinding
import com.kapitalletter.tastywardoffice.overview.OverviewViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class detail_googleMap : Fragment(), OnMapReadyCallback {

    private lateinit var mView: MapView
    private lateinit var GoogleMap: GoogleMap
    private lateinit var overViewModel: OverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overViewModel = ViewModelProvider(requireActivity()).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDetailGoogleMapBinding.inflate(inflater)
        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        GoogleMap = googleMap

        val current = arguments?.getParcelable<LatLng>("LatLng")
        Log.d("TestPar", current.toString())
        googleMap.addMarker(
            arguments?.getParcelable<LatLng>("LatLng")?.let {
                MarkerOptions()
                    .position(it)
                    .title(arguments?.getString("storeName"))
            }!!
        )!!.showInfoWindow()

        GoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current!!,16f))

    }

    override fun onStart() {
        super.onStart()
        mView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }

    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }

}