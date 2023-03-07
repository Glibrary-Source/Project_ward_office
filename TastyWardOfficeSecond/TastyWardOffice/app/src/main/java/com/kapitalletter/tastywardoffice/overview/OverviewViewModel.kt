package com.kapitalletter.tastywardoffice.overview


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kapitalletter.tastywardoffice.datamodel.*
import com.kapitalletter.tastywardoffice.network.*
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
    private val _filterState = MutableLiveData<String>()

    val distanceStoreData: LiveData<FinalStoreDataModel> get() = _distanceStoreData
    val cameraTarget: LiveData<LatLng> get() = _cameraTarget
    val markerStoreData: LiveData<Documents> get() = _markerStoreData
    val cameraZoom: LiveData<Float> get() = _cameraZoom
    val filterState: LiveData<String> get() = _filterState

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
                    _distanceStoreData.value = response.body()
                }
            }
            override fun onFailure(call: Call<FinalStoreDataModel>, t: Throwable) {

            }
        })
    }

    fun findStoreData(p0 : Marker): Documents {
        val passingData = distanceStoreData.value!!.Filterstore
        for (storeData in passingData) {
            val storePostion = LatLng(
                storeData.document.storeGEOPoints[0],
                storeData.document.storeGEOPoints[1]
            )
            if (storePostion == p0.position) {
                _markerStoreData.value = storeData.document
                return _markerStoreData.value!!
            }
        }
        return distanceStoreData.value!!.Filterstore[0].document
    }


    //현재시점 지도 포지션 저장하는 매서드
    fun saveCameraTarget(position: LatLng = LatLng(37.510402, 126.945915)) {
        _cameraTarget.value = position
    }

    //사용자가 사용중이던 지도의 카메라 줌상태를 저장함
    fun cameraZoomState(zoom: Float = 15f) {
        _cameraZoom.value = zoom
    }

    //필터 상태를 저장하는 매서드
    fun changeFilterState(filterState : String = "all") {
        _filterState.value = filterState
    }
}

