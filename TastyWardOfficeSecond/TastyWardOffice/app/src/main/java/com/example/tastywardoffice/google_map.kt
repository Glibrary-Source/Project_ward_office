package com.example.tastywardoffice

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tastywardoffice.databinding.FragmentGoogleMapBinding
import com.example.tastywardoffice.datamodel.LatLngData
import com.example.tastywardoffice.network.JoinData
import com.example.tastywardoffice.network.MyDTO
import com.example.tastywardoffice.network.TastyWardApi
import com.example.tastywardoffice.overview.OverviewViewModel
import com.example.tastywardoffice.overview.TestViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.maps.android.clustering.ClusterManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

//시작시 위치와 맵 정보 저장
private const val KEY_CAMERA_POSITION = "camera_position"
private const val KEY_LOCATION = "location"

class google_map : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private var latiTude = 37.56
    private var longItude = 126.97

    lateinit var geocoder: Geocoder
    lateinit var GoogleMap: GoogleMap
    lateinit var adress: MutableList<Address>
    lateinit var mContext: Context
    lateinit var storeName: String

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

        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGoogleMapBinding.inflate(inflater)
        mView = binding.mapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        checkLocationPermission()

        binding.myLocationButton.setOnClickListener {
            val current = LatLng(latiTude, longItude)
            checkLocationPermission()
            GoogleMap.addMarker(
                MarkerOptions()
                    .position(current)
                    .title("현재위치")
            )
        }

        Log.d(TAG, "onCreateView")

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        mView.onStart()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        GoogleMap = googleMap
        setDefaultLocation()
        aroundShop()
        googleMap?.setOnInfoWindowClickListener(this)


//        googleMap.setOnMarkerClickListener(this)

        geocoder = Geocoder(mContext, Locale.KOREA)
        adress = geocoder.getFromLocation(latiTude, longItude, 1)
    }

    //퍼미션 체크 기능인데 밑에있음
//    private fun checkPermissions() {
//        var rejectedPermissionList = ArrayList<String>()
//
//        for(permission in requiredPermissions) {
//            if(ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
//                rejectedPermissionList.add(permission)
//            }
//        }
//
//        if(rejectedPermissionList.isNotEmpty()) {
//            val array = arrayOfNulls<String>(rejectedPermissionList.size)
//            ActivityCompat.requestPermissions(mContext as Activity, rejectedPermissionList.toArray(array), multiplePermissionCode)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            multiplePermissionCode -> {
//                if(grantResults.isNotEmpty()) {
//                    for((i, permission) in permissions.withIndex()) {
//                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                            //권한 획득 실패
//                            Log.i("TAG", "The user has denied to $permission")
//                            Log.i("TAG", "I can't work for you anymore then. ByeBye!")
//                        }
//                    }
//                }
//            }
//        }
//    }


    //데이터 불러오기
    private fun aroundShop() {
        var storeLocation: LatLng
        val tempData = JoinData("filter_stores","E",5,4000,6000)
        TastyWardApi.service.getStoreData(tempData)?.enqueue(object : Callback<MyDTO>{
            override fun onResponse(call: Call<MyDTO>, response: Response<MyDTO>) {
                if (response.isSuccessful) {
                    var result: MyDTO? = response.body()
                    Log.d("YMC", " onResponse 성공 " + result?.secondData!!.storeId +" " + LatLng(response.body()!!.secondData.storeGEOPoints.latitude ,response.body()!!.secondData.storeGEOPoints.longitude))

                    storeName = result?.secondData!!.storeId

                    //스토어 위치 ex)"중구" 값
                    storeLocation = LatLng(response.body()!!.secondData.storeGEOPoints.latitude, response.body()!!.secondData.storeGEOPoints.longitude)

                    val geocoder = Geocoder(mContext, Locale.KOREA)
                    var address2 = geocoder.getFromLocation(37.56, 126.97, 1)
                    Log.d("YMC", " onResponse 성공 " + address2[0].subLocality)

                    //레트로핏으로 받아온 데이터를 구글맵 마크로 찍음
                    val sampleMarker = GoogleMap.addMarker(
                        MarkerOptions()
                            .position(storeLocation)
                            .title(storeName)
                            .snippet("김밥나라 입니다")
                    )
                    sampleMarker.showInfoWindow()

                } else {
                    var result: MyDTO? = response.body()
                    Log.d("YMC", "onResponse 실패 " + result?.toString())
                }
            }
            override fun onFailure(call: Call<MyDTO>, t: Throwable) {
                Log.d("YMC", "onFailure 에러 " + t.message.toString())
            }
        })
    }

    //지도 처음 띄웠을때 서울로 위치되도록
    private fun setDefaultLocation() {
        val defaultLocation = LatLng(latiTude, longItude)
        GoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,14f))
    }

    //퍼미션 체크 및 권한 요청 후 현위치로 이동 함수
    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "위치 허가 완료")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        latiTude = location.latitude
                        longItude = location.longitude
                        GoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latiTude,longItude),15f))
                        Log.d(TAG, "$latiTude , $longItude")
                    }
                }
        } else {
            var rejectedPermissionList = ArrayList<String>()

            for(permission in requiredPermissions) {
                if(ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    rejectedPermissionList.add(permission)
                }
            }

            if(rejectedPermissionList.isNotEmpty()) {
                val array = arrayOfNulls<String>(rejectedPermissionList.size)
                ActivityCompat.requestPermissions(mContext as Activity, rejectedPermissionList.toArray(array), multiplePermissionCode)
            }
            Toast.makeText(mContext, "위치권한이 없습니다..", Toast.LENGTH_SHORT).show()
        }
    }

    //마커클릭 이벤트
//    override fun onMarkerClick(Marker: Marker?): Boolean {
//        GoogleMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
//        return true
//    }


    override fun onInfoWindowClick(Marker: Marker?) {
        val action = google_mapDirections.actionGoogleMapToDetailMenu3(storeName,"https://i.picsum.photos/id/0/5000/3333.jpg?hmac=_j6ghY5fCfSD6tvtcV74zXivkJSPIfR9B8w34XeQmvU")
        findNavController().navigate(action)
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