package com.example.tastywardoffice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tastywardoffice.databinding.FragmentGoogleMapBinding
import com.example.tastywardoffice.overview.OverviewViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.util.*


class google_map : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMarkerClickListener {

    private var latiTude = 37.510402
    private var longItude = 126.945915

    lateinit var GoogleMap: GoogleMap
    lateinit var mContext: Context

    private lateinit var overViewModel: OverviewViewModel
    private lateinit var mView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val TAG = "MapFragment"
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
        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        overViewModel = ViewModelProvider(requireActivity()).get(OverviewViewModel::class.java)

        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentGoogleMapBinding.inflate(inflater)
        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        checkLocationPermission()

        //내위치 찍고 그쪽으로 카메라 이동
        binding.myLocationButton.setOnClickListener {
            checkLocationPermission()
            GoogleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(latiTude, longItude), 15f)
            )
        }

        binding.searchButton.setOnClickListener {
            binding.searchButton.setBackgroundColor(Color.parseColor("#afe3ff"))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        mView.onStart()
    }

    @SuppressLint("MissingPermission", "UseCompatLoadingForDrawables")
    override fun onMapReady(googleMap: GoogleMap) {
        GoogleMap = googleMap

        //이전에 사용자가 보던 위치
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(overViewModel.cameraTarget.value!!, 15f))

        //클릭 리스너들
        googleMap.setOnInfoWindowClickListener(this)
        googleMap.setOnMarkerClickListener(this)

        //마커 이미지 변경
//        val bitmapdraw = resources.getDrawable(R.drawable.ic_mark_image, null) as BitmapDrawable
//        val b = bitmapdraw.bitmap
//        val smallMarker = Bitmap.createScaledBitmap(b, 84, 84, false)


        //지도이동시 다시 storesdata 요청과 함께 요청된 데이터 좌표 마커찍기
        googleMap.setOnCameraIdleListener {

            //지도에서 마커 초기화
            googleMap.clear()

            //뷰모델에서 받아온 가게데이터 마커로 표시
            val position = GoogleMap.cameraPosition.target
            overViewModel.saveCameraTarget(position)
            overViewModel.distanceTo(overViewModel.cameraTarget.value!!)

            //범위원 그리기
            googleMap.addCircle(CircleOptions()
                .center(position)
                .radius(1000.0)
                .strokeColor(Color.BLACK)
            )

            //1초 뒤에
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    if(googleMap.cameraPosition.zoom >= 15){
                        Log.d("viewModelTest",  overViewModel.distanceStoreData.value!!.Howlong.toString())
                        for (i in overViewModel.distanceStoreData.value!!.Howlong) {
                            val storeLatLng =
                                LatLng(i.storeGEOPoints.latitude, i.storeGEOPoints.longitude)
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(i.storeId)
                            )
                        }
                    } else {
                        Toast.makeText(mContext, "카메라를 확대해주세요", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.d("viewModelTest", e.toString())
                }
            }, 1500)
        }

    }

    //퍼미션 체크 및 권한 요청 후 현위치로 이동 함수
    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(
//                mContext,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                mContext,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d(TAG, "위치 허가 완료")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        latiTude = location.latitude
                        longItude = location.longitude
                        Log.d(TAG, "$latiTude , $longItude")
                    } else {
                        Log.d(TAG, "fail")
                    }
                }
//        } else {
//            val rejectedPermissionList = ArrayList<String>()
//
//            for (permission in requiredPermissions) {
//                if (ContextCompat.checkSelfPermission(
//                        mContext,
//                        permission
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    rejectedPermissionList.add(permission)
//                }
//            }
//
//            if (rejectedPermissionList.isNotEmpty()) {
//                val array = arrayOfNulls<String>(rejectedPermissionList.size)
//                ActivityCompat.requestPermissions(
//                    mContext as Activity,
//                    rejectedPermissionList.toArray(array),
//                    multiplePermissionCode
//                )
//            }
//            Toast.makeText(mContext, "위치권한을 확인해 주세요", Toast.LENGTH_SHORT).show()
//            CameraUpdateFactory.newLatLngZoom(LatLng(latiTude, longItude), 15f)
//        }
    }

    //마커 정보 클릭시 세부 메뉴로 이동
    override fun onInfoWindowClick(p0: Marker) {
        fun findStoreData(): String {
            for(storedata in overViewModel.distanceStoreData.value!!.Howlong){
                val storePostion = LatLng(storedata.storeGEOPoints.latitude, storedata.storeGEOPoints.longitude)
                if(storePostion == p0.position) {
                    return storedata.storeMenuPictureUrls.menu[0]
                }
            }
            return "아님"
        }
        val action = google_mapDirections.actionGoogleMapToDetailMenu3(
            p0.title.toString(),
            findStoreData(),
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
        Log.d(TAG, "onResume")
        mView.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        mView.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")

        mView.onStop()
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

//    private fun totalShopData() {
//        val tempData = RequestType("whole_stores")
//        TastyWardApi.service.getWholeData(tempData).enqueue(object : Callback<WholeData> {
//            override fun onResponse(call: Call<WholeData>, response: Response<WholeData>) {
//                if (response.isSuccessful) {
//                    Log.d("wholedata", response.body()!!.toString())
//                    for (i in response.body()!!.stores) {
//                        GoogleMap.addMarker(
//                            MarkerOptions()
//                                .position(
//                                    LatLng(
//                                        i.storeGEOPoints.latitude,
//                                        i.storeGEOPoints.longitude
//                                    )
//                                )
//                                .title(i.storeId)
//                                .snippet("성공입니다")
//                        )?.showInfoWindow()
//                    }
//                } else {
//                    val result: WholeData? = response.body()
//                    Log.d("wholedata", "onResponse 실패 " + result?.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<WholeData>, t: Throwable) {
//                Log.d("wholedata", "onFailure 에러 " + t.message.toString())
//            }
//        })
//    }


    //데이터 불러오기
//    private fun aroundShop() {
//        var storeLocation: LatLng
//        val tempData = JoinData("filter_stores", "E", 5, 4000, 6000)
//        TastyWardApi.service.getStoreData(tempData).enqueue(object : Callback<MyDTO> {
//            override fun onResponse(call: Call<MyDTO>, response: Response<MyDTO>) {
//                if (response.isSuccessful) {
//                    val result: MyDTO? = response.body()
//                    Log.d(
//                        "YMC",
//                        " onResponse 성공 " + result?.secondData!!.storeId + " " + LatLng(
//                            response.body()!!.secondData.storeGEOPoints.latitude,
//                            response.body()!!.secondData.storeGEOPoints.longitude
//                        ) + " " + response.body()!!.secondData.storeMenuPictureUrls
//                    )
//
//                    storeName = result.secondData.storeId
//                    testDTO = result
//
//                    storeLocation = LatLng(
//                        response.body()!!.secondData.storeGEOPoints.latitude,
//                        response.body()!!.secondData.storeGEOPoints.longitude
//                    )
//
//                    //스토어 위치 ex)"중구" 값
//                    val geocoder = Geocoder(mContext, Locale.KOREA)
//                    val address2 = geocoder.getFromLocation(storeLocation.latitude, storeLocation.longitude,1)
//                    Log.d("YMC", " onResponse 성공 " + address2[0].subLocality)
//
//                } else {
//                    val result: MyDTO? = response.body()
//                    Log.d("YMC", "onResponse 실패 " + result?.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<MyDTO>, t: Throwable) {
//                Log.d("YMC", "onFailure 에러 " + t.message.toString())
//            }
//        })
//    }


}