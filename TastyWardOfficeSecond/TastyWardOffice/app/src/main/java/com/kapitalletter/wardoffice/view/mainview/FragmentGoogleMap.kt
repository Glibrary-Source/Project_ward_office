package com.kapitalletter.wardoffice.view.mainview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.kapitalletter.wardoffice.databinding.FragmentGoogleMapBinding
import com.kapitalletter.wardoffice.databinding.FragmentGoogleMapFilterButtonBinding
import com.kapitalletter.wardoffice.databinding.FragmentGoogleMapMenuButtonBinding
import com.kapitalletter.wardoffice.view.mainview.util.AdmobController
import com.kapitalletter.wardoffice.view.mainview.util.CheckPermission
import com.kapitalletter.wardoffice.view.mainview.util.MapController
import com.kapitalletter.wardoffice.view.mainview.util.MapFilterButtonController
import com.kapitalletter.wardoffice.view.mainview.viewmodel.OverviewViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FragmentGoogleMap : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private lateinit var GoogleMap: GoogleMap
    private lateinit var overViewModel: OverviewViewModel
    private lateinit var binding: FragmentGoogleMapBinding
    private lateinit var callback: OnBackPressedCallback

    private lateinit var mView: MapView
    private lateinit var filterMenuView: FragmentGoogleMapMenuButtonBinding
    private lateinit var filterLocalView: FragmentGoogleMapFilterButtonBinding


    private lateinit var mapController: MapController
    private lateinit var adMobController: AdmobController
    private lateinit var mapFilterButtonController: MapFilterButtonController

    private val permissionModule = CheckPermission()
    private var backPressTime: Long = 0
    private var toast: Toast? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setBackPressCallBack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]
        adMobController = AdmobController(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoogleMapBinding.inflate(inflater)

        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        filterMenuView = binding.includeBtnMenubar
        filterLocalView = binding.includeBtnFilterbar

        permissionModule.checkLocationPermission(requireContext())

        binding.myLocationButton.setOnClickListener {
            try {
                moveCurrentLocation()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "위치 권한을 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        GoogleMap = googleMap

        mapController = MapController(GoogleMap, requireContext())
        mapFilterButtonController = MapFilterButtonController(
            GoogleMap,
            overViewModel,
            filterMenuView,
            filterLocalView,
            mapController,
            requireContext()
        )

        mapController.mapLimitBoundaryKorea()
        mapController.moveUserViewLocation(
            overViewModel.cameraTarget.value!!,
            overViewModel.cameraZoom.value!!
        )

        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setOnMarkerClickListener(this)

        googleMap.setOnCameraIdleListener {
            googleMap.clear()
            setTargetAndZoom()
            callAroundStoreData()
            mapController.addMarkCurrentLocation(permissionModule)

            mapFilterButtonController.filterViewExpandClose()
        }

        overViewModel.distanceStoreData.observe(this) {
            createMarkerAroundShop()

            if (overViewModel.distanceStoreData.value!!.filterStore.isEmpty()) {
                toastEmptyStore()
            }
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        try {
            adMobController.mapInfoClickCallAd()

            overViewModel.findStoreData(p0)

            findNavController().navigate(
                mapController.mapInfoClickAction(p0, overViewModel.markerStoreData.value!!.docId)
            )
        } catch (e: Exception) { }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        p0.showInfoWindow()
        return true
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }

    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        binding.includeBtnFilterbar.filterWardOfficeButton.setOnClickListener(this)
        binding.includeBtnMenubar.titleButton.setOnClickListener(this)

        binding.includeBtnMenubar.filterButtonKorean.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonJapan.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonChina.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonDessert.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonKimbap.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonWestern.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonAsian.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonChiken.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonBar.setOnClickListener(this)
        binding.includeBtnMenubar.filterButtonAll.setOnClickListener(this)

        binding.includeBtnFilterbar.filterButtonGangnam.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonGandong.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonGangbuk.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonGangseo.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonGwanak.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonGwangjin.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonGuro.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonGeumcheon.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonNowon.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonDobong.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonDdm.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonDongjak.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonMapo.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonSdm.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonSeocho.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonSd.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonSb.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonSongpa.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonYangcheon.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonYdp.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonYongsan.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonEp.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonJongno.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonJunggu.setOnClickListener(this)
        binding.includeBtnFilterbar.filterButtonJungnang.setOnClickListener(this)

        mView.onStart()
    }

    override fun onClick(p0: View?) {
        mapFilterButtonController.setFilterOnClickButton(p0)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun setBackPressCallBack() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressTime + 3000 > System.currentTimeMillis()) {
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "한번 더 뒤로가기 버튼을 누르면 종료됩니다", Toast.LENGTH_SHORT)
                        .show()
                }
                backPressTime = System.currentTimeMillis()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun moveCurrentLocation() {
        permissionModule.checkLocationPermission(requireContext())
        GoogleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(permissionModule.latitude, permissionModule.longitude),
                GoogleMap.cameraPosition.zoom
            )
        )
    }

    private fun setTargetAndZoom() {
        overViewModel.setCameraTargetAndZoom(
            GoogleMap.cameraPosition.target,
            GoogleMap.cameraPosition.zoom
        )
    }

    private fun callAroundStoreData() {
        CoroutineScope(IO).launch {
            overViewModel.setDistanceToData(overViewModel.cameraTarget.value!!)
        }
    }

    private fun toastEmptyStore() {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), "이 위치에는 가게가 없습니다.\n지도를 이동해주세요", Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun createMarkerAroundShop() {
        try {
            for (i in overViewModel.distanceStoreData.value!!.filterStore) {
                mapController.createFilterStateMarker(overViewModel.filterState.value!!, i)
            }
        } catch (e: Exception) {

        }
    }

}