package com.kapitalletter.wardoffice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.kapitalletter.wardoffice.data.WardOfficeGeo
import com.kapitalletter.wardoffice.databinding.FragmentGoogleMapBinding
import com.kapitalletter.wardoffice.overview.OverviewViewModel


class google_map : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private var latiTude = 37.510402
    private var longItude = 126.945915

    private lateinit var GoogleMap: GoogleMap
    private lateinit var mContext: Context
    private lateinit var overViewModel: OverviewViewModel
    private lateinit var mView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentGoogleMapBinding
    private lateinit var callback: OnBackPressedCallback

    private val locationGeoData = WardOfficeGeo()

    var backPressTime: Long = 0

    private val multiplePermissionCode = 100
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var toast: Toast? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressTime + 3000 > System.currentTimeMillis()) {
                    requireActivity().finish()
                } else {
                    Toast.makeText(mContext, "한번 더 뒤로가기 버튼을 누르면 종료됩니다", Toast.LENGTH_SHORT)
                        .show()
                }
                backPressTime = System.currentTimeMillis()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        overViewModel = ViewModelProvider(requireActivity())[OverviewViewModel::class.java]

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGoogleMapBinding.inflate(inflater)

        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        checkLocationPermission()

        binding.myLocationButton.setOnClickListener {
            try {
                checkLocationPermission()
                GoogleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(latiTude, longItude),
                        GoogleMap.cameraPosition.zoom
                    )
                )
            } catch (e: Exception) {
                Toast.makeText(mContext, "위치 권한을 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    @SuppressLint("MissingPermission", "UseCompatLoadingForDrawables", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        GoogleMap = googleMap

        val builder = LatLngBounds.Builder()
        builder.include(LatLng(33.1422, 124.0384))
        builder.include(LatLng(38.6120, 131.2361))
        val bounds = builder.build()
        GoogleMap.setLatLngBoundsForCameraTarget(bounds)

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                overViewModel.cameraTarget.value!!,
                overViewModel.cameraZoom.value!!
            )
        )

        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setOnMarkerClickListener(this)

        googleMap.setOnCameraIdleListener {

            googleMap.clear()
            getOverviewLocationData()

            val currentDrawable =
                resources.getDrawable(R.drawable.marker_icons_mylocation, null) as BitmapDrawable
            val img = currentDrawable.bitmap
            val currentLocationMarker = Bitmap.createScaledBitmap(img, 80, 80, false)

            GoogleMap.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(currentLocationMarker))
                    .position(LatLng(latiTude, longItude))
                    .title("현위치")
            )

            binding.layoutExpand.visibility = View.GONE
            binding.layoutExpand2.visibility = View.GONE
            binding.layoutExpand3.visibility = View.GONE
        }

        overViewModel.distanceStoreData.observe(this) {
            second()
            if (overViewModel.distanceStoreData.value!!.Filterstore.isEmpty()) {
                toast?.cancel()
                toast = Toast.makeText(mContext, "이 위치에는 가게가 없습니다.\n지도를 이동해주세요", Toast.LENGTH_SHORT)
                toast?.show()
            }
        }
    }

    private fun getOverviewLocationData() {
        val position = GoogleMap.cameraPosition.target
        overViewModel.cameraZoomState(GoogleMap.cameraPosition.zoom)
        overViewModel.saveCameraTarget(position)
        overViewModel.distanceTo(overViewModel.cameraTarget.value!!)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun second() {
        try {
            for (i in overViewModel.distanceStoreData.value!!.Filterstore) {
                val storeLatLng =
                    LatLng(i.document.storeGEOPoints[0], i.document.storeGEOPoints[1])

                val storeDrawable =
                    when (i.document.storeTitle) {
                        getString(R.string.japan) -> R.drawable.marker_icons_food_sushi
                        getString(R.string.chines) -> R.drawable.marker_icons_food_china
                        getString(R.string.korean) -> R.drawable.marker_icons_food_korean
                        getString(R.string.kimbap) -> R.drawable.marker_icons_food_kimbap
                        getString(R.string.western) -> R.drawable.marker_icons_food_western
                        getString(R.string.dessert) -> R.drawable.marker_icons_food_coffee
                        getString(R.string.asian) -> R.drawable.marker_icons_food_asian
                        getString(R.string.chiken) -> R.drawable.marker_icons_food_chiken
                        getString(R.string.bar) -> R.drawable.marker_icons_food_bar
                        else -> R.drawable.marker_icons_food_sushi
                    }
                val bitMapDraw =
                    resources.getDrawable(storeDrawable, null) as BitmapDrawable
                val b = bitMapDraw.bitmap
                val smallMarker = Bitmap.createScaledBitmap(b, 84, 84, false)

                when (overViewModel.filterState.value) {
                    getString(R.string.korean) -> {
                        if (i.document.storeTitle == getString(R.string.korean)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )

                        }
                    }
                    getString(R.string.japan) -> {
                        if (i.document.storeTitle == getString(R.string.japan)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )
                        }
                    }
                    getString(R.string.chines) -> {
                        if (i.document.storeTitle == getString(R.string.chines)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )
                        }
                    }
                    getString(R.string.kimbap) -> {
                        if (i.document.storeTitle == getString(R.string.kimbap)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )
                        }
                    }
                    getString(R.string.western) -> {
                        if (i.document.storeTitle == getString(R.string.western)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )
                        }
                    }
                    getString(R.string.dessert) -> {
                        if (i.document.storeTitle == getString(R.string.dessert)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )
                        }
                    }
                    getString(R.string.asian) -> {
                        if (i.document.storeTitle == getString(R.string.asian)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )
                        }
                    }
                    getString(R.string.chiken) -> {
                        if (i.document.storeTitle == getString(R.string.chiken)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )
                        }
                    }
                    getString(R.string.bar) -> {
                        if (i.document.storeTitle == getString(R.string.bar)) {
                            GoogleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.document.storeId)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            )
                        }
                    }
                    else -> {
                        GoogleMap.addMarker(
                            MarkerOptions()
                                .position(storeLatLng)
                                .title(i.document.storeId)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        )

                    }
                }
            }
        } catch (e: Exception) {

        }
    }


    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        latiTude = location.latitude
                        longItude = location.longitude
                    }
                }
        } else {
            val rejectedPermissionList = ArrayList<String>()

            for (permission in requiredPermissions) {
                if (ContextCompat.checkSelfPermission(
                        mContext,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    rejectedPermissionList.add(permission)
                }
            }

            if (rejectedPermissionList.isNotEmpty()) {
                val array = arrayOfNulls<String>(rejectedPermissionList.size)
                ActivityCompat.requestPermissions(
                    mContext as Activity,
                    rejectedPermissionList.toArray(array),
                    multiplePermissionCode
                )
            }
            Toast.makeText(mContext, getString(R.string.locationcheck), Toast.LENGTH_SHORT).show()
            CameraUpdateFactory.newLatLngZoom(LatLng(latiTude, longItude), 17f)
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        try {
            if (MyGlobals.instance?.adMobCount!! % 5 == 0) {
                MyGlobals.instance?.fullAD!!.show(mContext as Activity)
            }
            overViewModel.findStoreData(p0)
            val action = google_mapDirections.actionGoogleMapToDetailMenu3(
                p0.title.toString(),
                overViewModel.markerStoreData.value!!.docId,
                p0.position
            )
            findNavController().navigate(action)
        } catch (e: Exception) {

        }
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


        binding.filterWardOfficeButton.setOnClickListener(this)
        binding.titleButton.setOnClickListener(this)


        binding.filterButtonKorean.setOnClickListener(this)
        binding.filterButtonJapan.setOnClickListener(this)
        binding.filterButtonChina.setOnClickListener(this)
        binding.filterButtonDessert.setOnClickListener(this)
        binding.filterButtonKimbap.setOnClickListener(this)
        binding.filterButtonWestern.setOnClickListener(this)
        binding.filterButtonAsian.setOnClickListener(this)
        binding.filterButtonChiken.setOnClickListener(this)
        binding.filterButtonBar.setOnClickListener(this)
        binding.filterButtonAll.setOnClickListener(this)


        binding.filterButtonGangnam.setOnClickListener(this)
        binding.filterButtonGandong.setOnClickListener(this)
        binding.filterButtonGangbuk.setOnClickListener(this)
        binding.filterButtonGangseo.setOnClickListener(this)
        binding.filterButtonGwanak.setOnClickListener(this)
        binding.filterButtonGwangjin.setOnClickListener(this)
        binding.filterButtonGuro.setOnClickListener(this)
        binding.filterButtonGeuncheon.setOnClickListener(this)
        binding.filterButtonNowon.setOnClickListener(this)
        binding.filterButtonDobong.setOnClickListener(this)
        binding.filterButtonDdm.setOnClickListener(this)
        binding.filterButtonDongjak.setOnClickListener(this)
        binding.filterButtonMapo.setOnClickListener(this)
        binding.filterButtonSdm.setOnClickListener(this)
        binding.filterButtonSeocho.setOnClickListener(this)
        binding.filterButtonSd.setOnClickListener(this)
        binding.filterButtonSb.setOnClickListener(this)
        binding.filterButtonSongpa.setOnClickListener(this)
        binding.filterButtonYangcheon.setOnClickListener(this)
        binding.filterButtonYdp.setOnClickListener(this)
        binding.filterButtonYongsan.setOnClickListener(this)
        binding.filterButtonEp.setOnClickListener(this)
        binding.filterButtonJongno.setOnClickListener(this)
        binding.filterButtonJunggu.setOnClickListener(this)
        binding.filterButtonJungnang.setOnClickListener(this)

        mView.onStart()
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.filter_button_all -> {
                overViewModel.changeFilterState(getString(R.string.all))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_korean -> {
                overViewModel.changeFilterState(getString(R.string.korean))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_china -> {
                overViewModel.changeFilterState(getString(R.string.chines))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_japan -> {
                overViewModel.changeFilterState(getString(R.string.japan))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_kimbap -> {
                overViewModel.changeFilterState(getString(R.string.kimbap))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_western -> {
                overViewModel.changeFilterState(getString(R.string.western))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_dessert -> {
                overViewModel.changeFilterState(getString(R.string.dessert))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_asian -> {
                overViewModel.changeFilterState(getString(R.string.asian))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_chiken -> {
                overViewModel.changeFilterState(getString(R.string.chiken))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.filter_button_bar -> {
                overViewModel.changeFilterState(getString(R.string.bar))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }


            R.id.title_button -> {
                if (binding.layoutExpand.visibility == View.VISIBLE) {
                    binding.layoutExpand.visibility = View.GONE


                    binding.layoutExpand2.visibility = View.GONE
                    binding.layoutExpand3.visibility = View.GONE
                    binding.total.visibility = View.GONE

                    binding.imgMore1.animate().duration = 200
                } else {
                    binding.layoutExpand.visibility = View.VISIBLE


                    binding.layoutExpand2.visibility = View.GONE
                    binding.layoutExpand3.visibility = View.GONE
                    binding.total.visibility = View.GONE

                    binding.imgMore1.animate().duration = 200
                }
            }
            R.id.filter_ward_office_button -> {
                if (binding.layoutExpand2.visibility == View.VISIBLE &&
                    binding.layoutExpand3.visibility == View.VISIBLE &&
                    binding.total.visibility == View.VISIBLE
                ) {
                    binding.layoutExpand2.visibility = View.GONE
                    binding.layoutExpand3.visibility = View.GONE
                    binding.total.visibility = View.GONE


                    binding.layoutExpand.visibility = View.GONE

                    binding.imgMore1.animate().duration = 200
                } else {
                    binding.layoutExpand2.visibility = View.VISIBLE
                    binding.layoutExpand3.visibility = View.VISIBLE
                    binding.total.visibility = View.VISIBLE


                    binding.layoutExpand.visibility = View.GONE

                    binding.imgMore1.animate().duration = 200
                }
            }
            R.id.filter_button_gangnam -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.gangnam)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_gandong -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.gangdong)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_gangbuk -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.gangbuk)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_gangseo -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.gangseo)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_gwanak -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.gwanak)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_gwangjin -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.gwangjin)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_guro -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.guro)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_geuncheon -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.geumcheon)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_nowon -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.nowon)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_dobong -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.dobong)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_ddm -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.ddm)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_dongjak -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.dongjak)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_mapo -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.mapo)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_sdm -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.sdm)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_seocho -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.seocho)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_sd -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.sd)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_sb -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.sb)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_songpa -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.songpa)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_yangcheon -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.yangcheon)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_ydp -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.ydp)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_yongsan -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.yongsan)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_ep -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.ep)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_jongno -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.jongno)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_junggu -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.junggu)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            R.id.filter_button_jungnang -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.jungnang)
                        ), 17f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }

            else -> {
                overViewModel.changeFilterState(getString(R.string.dessert))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}