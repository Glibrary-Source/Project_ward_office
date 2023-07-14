package com.kapitalletter.wardoffice.view.mainview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kapitalletter.wardoffice.databinding.FragmentDetailGoogleMapBinding
import com.kapitalletter.wardoffice.view.mainview.viewmodel.OverviewViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.kapitalletter.wardoffice.MyGlobals
import com.kapitalletter.wardoffice.view.mainview.util.DetailGoogleMapMarker

class FragmentDetailGoogleMap : Fragment(), OnMapReadyCallback {

    private lateinit var mView: MapView
    private lateinit var GoogleMap: GoogleMap
    private lateinit var overViewModel: OverviewViewModel
    private lateinit var detailGoogleMapMarker: DetailGoogleMapMarker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]
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
        detailGoogleMapMarker = DetailGoogleMapMarker(GoogleMap)
        detailGoogleMapMarker.setDetailGoogleMap()
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