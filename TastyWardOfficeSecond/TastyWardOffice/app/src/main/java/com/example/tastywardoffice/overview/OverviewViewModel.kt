package com.example.tastywardoffice.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tastywardoffice.BuildConfig.MAPS_API_KEY
import com.example.tastywardoffice.datamodel.*
import com.example.tastywardoffice.network.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverviewViewModel : ViewModel() {

    private val _distanceStoreData = MutableLiveData<FinalStoreDataModel>()
    private val _cameraTarget = MutableLiveData<LatLng>()
    private val _markerStoreData = MutableLiveData<Documents>()
    private val _cameraZoom = MutableLiveData<Float>()
    private val _locationDetail = MutableLiveData<String>()
    private val _filterState = MutableLiveData<String>()

    val distanceStoreData: LiveData<FinalStoreDataModel> = _distanceStoreData
    val cameraTarget: LiveData<LatLng> = _cameraTarget
    val markerStoreData: LiveData<Documents> = _markerStoreData
    val cameraZoom: LiveData<Float> = _cameraZoom
    val locationDetail: LiveData<String> = _locationDetail
    val filterState: LiveData<String> = _filterState
    init {
        saveCameraTarget()
        cameraZoomState()
        changeFilterState()
    }

    fun distanceTo(position: LatLng = LatLng(37.510402, 126.945915)) {
        val myLocation = listOf(position.latitude, position.longitude)
        val requestType = RequestLocationData("How_long", myLocation)
        TastyWardApi.service.getLocationDistanceTo(requestType).enqueue(object : Callback<FinalStoreDataModel> {
            override fun onResponse(call: retrofit2.Call<FinalStoreDataModel>, response: Response<FinalStoreDataModel>) {
                if (response.isSuccessful) {
                    Log.d("LocationDB", position.toString())
                    _distanceStoreData.value = response.body()
                } else {
                    val result: FinalStoreDataModel? = response.body()
                    Log.d("LocationDB", "onResponse 실패 " + result?.toString())
                }
            }
            override fun onFailure(call: retrofit2.Call<FinalStoreDataModel>, t: Throwable) {
                Log.d("LocationDB", "onFailure 에러 " + t.message.toString())
            }
        })
    }

    fun saveCameraTarget(position: LatLng = LatLng(37.510402, 126.945915)) {
        _cameraTarget.value = position
    }

    fun findStoreData(p0 : Marker): Documents {
        val passingData = distanceStoreData.value!!.Filterstore
        for (storedata in passingData) {
            val storePostion = LatLng(
                storedata.document.storeGEOPoints[0],
                storedata.document.storeGEOPoints[1]
            )
            if (storePostion == p0.position) {
                _markerStoreData.value = storedata.document
                return _markerStoreData.value!!
            }
        }
        return distanceStoreData.value!!.Filterstore[0].document
    }

    fun cameraZoomState(zoom: Float = 15f) {
        _cameraZoom.value = zoom
    }

    fun locationTestApi(location: String) {
        val key = MAPS_API_KEY
        TastyWardApi2.service.getDetailLocation(location,key,"ko").enqueue(object : Callback<LocationDetailData> {
            override fun onResponse(call: Call<LocationDetailData>, response: Response<LocationDetailData>) {
                if (response.isSuccessful) {
                    _locationDetail.value = response.body()!!.results[0].formatted_address
                } else {
                    val result: LocationDetailData? = response.body()
                    Log.d("wholedata", "onResponse 실패 " + result?.toString())
                }
            }
            override fun onFailure(call: Call<LocationDetailData>, t: Throwable) {
                Log.d("wholedata", "onFailure 에러 " + t.message.toString())
            }
        })
    }

    fun changeFilterState(filterState : String = "all") {
        _filterState.value = filterState
    }


}


//데이터 개수 또는 조건문을 통한 데이터 개수 제한
//    fun filterMenu() {
//        val testMutableList = mutableListOf<TastyPhoto>()
//        for(value in _photos.value!!){
//            if (value.id.toInt() <= 424909) {
//                testMutableList.add(value)
//            }
//        }
//        _photos.value = testMutableList
//    }
