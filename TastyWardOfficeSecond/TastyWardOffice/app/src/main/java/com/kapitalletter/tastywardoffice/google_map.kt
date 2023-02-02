package com.kapitalletter.tastywardoffice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kapitalletter.tastywardoffice.databinding.FragmentGoogleMapBinding
import com.kapitalletter.tastywardoffice.data.WardOfficeGeo
import com.kapitalletter.tastywardoffice.overview.OverviewViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

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

    private val locationGeoData = WardOfficeGeo()

    //    퍼미션 체크
    private val multiplePermissionCode = 100
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        overViewModel = ViewModelProvider(requireActivity()).get(OverviewViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGoogleMapBinding.inflate(inflater)
        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        //내위치 확인
        checkLocationPermission()

        //내위치 찍고 그쪽으로 카메라 이동
        binding.myLocationButton.setOnClickListener {
            checkLocationPermission()
            GoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latiTude, longItude),
                    GoogleMap.cameraPosition.zoom
                )
            )
        }

        return binding.root
    }

    @SuppressLint("MissingPermission", "UseCompatLoadingForDrawables", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        GoogleMap = googleMap

        //이전에 사용자가 보던 위치
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                overViewModel.cameraTarget.value!!,
                overViewModel.cameraZoom.value!!
            )
        )

        //클릭 리스너들
        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setOnMarkerClickListener(this)

        //지도이동시 다시 storesdata 요청과 함께 요청된 데이터 좌표 마커찍기
        googleMap.setOnCameraIdleListener {

            //지도에서 마커 초기화
            googleMap.clear()
            callback()
        }

        overViewModel.distanceStoreData.observe(this) {
            second()
            if (overViewModel.distanceStoreData.value!!.Filterstore.isEmpty()) {
                val simpleToast =
                    Toast.makeText(mContext, "이 위치에는 가게가 없습니다.\n지도를 이동해주세요", Toast.LENGTH_SHORT)
                simpleToast.setGravity(Gravity.CENTER, 0, 0)
                simpleToast.show()
            }
        }
    }

    //서버에서 스토어 데이터를 미리 불러옴
    private fun callback() {
        val position = GoogleMap.cameraPosition.target
        overViewModel.cameraZoomState(GoogleMap.cameraPosition.zoom)
        overViewModel.saveCameraTarget(position)
        overViewModel.distanceTo(overViewModel.cameraTarget.value!!)
    }

    //불러온 distanceStoreData를 마커로 찍어줌
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun second() {
        try {
            for (i in overViewModel.distanceStoreData.value!!.Filterstore) {
                val storeLatLng =
                    LatLng(i.document.storeGEOPoints[0], i.document.storeGEOPoints[1])

                //마커 이미지 변경
                val storeDrawable =
                    when (i.document.storeTitle) {
                        getString(R.string.japan) -> {
                            R.drawable.marker_icons_food_sushi
                        }
                        getString(R.string.chines) -> R.drawable.marker_icons_food_china
                        getString(R.string.korean) -> R.drawable.marker_icons_food_korean
                        getString(R.string.kimbap) -> R.drawable.marker_icons_food_kimbap
                        getString(R.string.cafe) -> R.drawable.marker_icons_food_coffee
                        else -> R.drawable.marker_icons_food_sushi
                    }
                val bitMapDraw =
                    resources.getDrawable(storeDrawable, null) as BitmapDrawable
                val b = bitMapDraw.bitmap
                val smallMarker = Bitmap.createScaledBitmap(b, 84, 84, false)

                //지도에 마커 작성
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
                    getString(R.string.cafe) -> {
                        if (i.document.storeTitle == getString(R.string.cafe)) {
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
            Toast.makeText(mContext, overViewModel.distanceStoreData.value.toString() + e.toString() , Toast.LENGTH_SHORT).show()

        }
    }


    //퍼미션 체크 및 권한 요청 후 현위치로 이동 함수
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
            Toast.makeText(mContext, "위치권한을 확인해 주세요", Toast.LENGTH_SHORT).show()
            CameraUpdateFactory.newLatLngZoom(LatLng(latiTude, longItude), 15f)
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        overViewModel.findStoreData(p0)
        Log.d("MarkerTest", overViewModel.markerStoreData.value!!.docId)
        val action = google_mapDirections.actionGoogleMapToDetailMenu3(
            p0.title.toString(),
            overViewModel.markerStoreData.value!!.docId,
            p0.position
        )
        findNavController().navigate(action)
    }

    //마커 클릭시 그쪽으로 확대 멈춤
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

        //메뉴, 지역 필터 버튼
        binding.filterWardOfficeButton.setOnClickListener(this)
        binding.titleButton.setOnClickListener(this)

        //메뉴 필터
        binding.filterButtonKorean.setOnClickListener(this)
        binding.filterButtonJapan.setOnClickListener(this)
        binding.filterButtonChina.setOnClickListener(this)
        binding.filterButtonCafe.setOnClickListener(this)
        binding.filterButtonKimbap.setOnClickListener(this)
        binding.filterButtonAll.setOnClickListener(this)

        //지역 필터
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

    //필터 클릭리스너
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
            R.id.filter_button_cafe -> {
                overViewModel.changeFilterState(getString(R.string.cafe))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
            R.id.title_button -> {
                if (binding.layoutExpand.visibility == View.VISIBLE) {
                    binding.layoutExpand.visibility = View.GONE

                    //메뉴 클릭시 지역 필터 레이아웃 끄기
                    binding.layoutExpand2.visibility = View.GONE
                    binding.layoutExpand3.visibility = View.GONE
                    binding.total.visibility = View.GONE

                    binding.imgMore1.animate().duration = 200
                } else {
                    binding.layoutExpand.visibility = View.VISIBLE

                    //메뉴 클릭시 지역 필터 레이아웃 끄기
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

                    //지역 클릭시 메뉴 필터 레이아웃 끄기
                    binding.layoutExpand.visibility = View.GONE

                    binding.imgMore1.animate().duration = 200
                } else {
                    binding.layoutExpand2.visibility = View.VISIBLE
                    binding.layoutExpand3.visibility = View.VISIBLE
                    binding.total.visibility = View.VISIBLE

                    //지역 클릭시 메뉴 필터 레이아웃 끄기
                    binding.layoutExpand.visibility = View.GONE

                    binding.imgMore1.animate().duration = 200
                }
            }
            R.id.filter_button_gangnam -> {
                GoogleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        locationGeoData.getWardGeo(
                            getString(R.string.gangnam)
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
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
                        ), 15f
                    )
                )
                binding.layoutExpand2.visibility = View.GONE
                binding.layoutExpand3.visibility = View.GONE
            }
            else -> {
                overViewModel.changeFilterState(getString(R.string.cafe))
                binding.layoutExpand.visibility = View.GONE
                GoogleMap.clear()
                second()
            }
        }
    }
}