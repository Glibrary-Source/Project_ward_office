package com.kapitalletter.tastywardoffice.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kapitalletter.tastywardoffice.BuildConfig
import com.kapitalletter.tastywardoffice.datamodel.*
import com.kapitalletter.tastywardoffice.network.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

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

    //거리별 음식점 마커 불러오는 매서드
    fun distanceTo(position: LatLng = LatLng(37.510402, 126.945915)) {
        val myLocation = listOf(position.latitude, position.longitude)
        val requestType = RequestLocationData("How_long", myLocation)
        TastyWardApi.service.getLocationDistanceTo(requestType).enqueue(object : Callback<FinalStoreDataModel> {
            override fun onResponse(call: Call<FinalStoreDataModel>, response: Response<FinalStoreDataModel>) {
                if (response.isSuccessful) {
                    Log.d("LocationDB", position.toString())
                    Log.d("LocationDB", response.body().toString())
                    _distanceStoreData.value = response.body()
                } else {
                    val result: FinalStoreDataModel? = response.body()
                    Log.d("LocationDB", "onResponse 실패 " + result?.toString())
                }
            }
            override fun onFailure(call: Call<FinalStoreDataModel>, t: Throwable) {
                Log.d("LocationDB", "onFailure 에러 " + t.message.toString())
            }
        })
    }

    //현재시점 지도 포지션 저장하는 매서드
    fun saveCameraTarget(position: LatLng = LatLng(37.510402, 126.945915)) {
        _cameraTarget.value = position
    }

    //사용자가 사용중이던 지도의 카메라 줌상태를 저장함
    fun cameraZoomState(zoom: Float = 15f) {
        _cameraZoom.value = zoom
    }

    //현재 마커 기준으로 서버에서 받아온 스토어 데이터를 찾아주는 메서드
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

    //위치데이터를 받아올수 있는지 시험하는 매서드 일단은 보류하자
    fun locationTestApi(location: String) {
        val key = BuildConfig.MAPS_DETAIL_LOCATION_KEY
        TastyWardApi2.service.getDetailLocation(location,key,"ko").enqueue(object : Callback<LocationDetailData> {
            override fun onResponse(call: Call<LocationDetailData>, response: Response<LocationDetailData>) {
                try{
                    if (response.isSuccessful) {
                        _locationDetail.value = response.body()!!.results[0].formatted_address
                    }
                }catch (e: Exception) {
                    Log.d("detailLocationTest", e.message.toString() + location)
                }
            }
            override fun onFailure(call: Call<LocationDetailData>, t: Throwable) {
                Log.d("wholedata", "onFailure 에러 " + t.message.toString())
            }
        })
    }

    //필터 상태를 저장하는 매서드
    fun changeFilterState(filterState : String = "all") {
        _filterState.value = filterState
    }

}

