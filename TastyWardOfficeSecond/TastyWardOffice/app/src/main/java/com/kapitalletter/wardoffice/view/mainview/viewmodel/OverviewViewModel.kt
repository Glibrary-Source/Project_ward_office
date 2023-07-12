package com.kapitalletter.wardoffice.view.mainview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kapitalletter.wardoffice.datamodel.*
import com.kapitalletter.wardoffice.network.*
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
        setCameraTargetAndZoom()
        changeFilterState()
    }

    suspend fun setDistanceToData(position: LatLng = LatLng(37.510402, 126.945915)) {
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

    fun getDistanceToData() : FinalStoreDataModel {
        return try {
            distanceStoreData.value!!
        } catch (e: Exception) {
            FinalStoreDataModel(listOf())
        }
    }

    fun findStoreData(p0 : Marker) {
        val passingData = distanceStoreData.value!!.filterStore
        for (storeData in passingData) {
            val storePosition = LatLng(
                storeData.document.storeGEOPoints[0],
                storeData.document.storeGEOPoints[1]
            )
            if (storePosition == p0.position) {
                _markerStoreData.value = storeData.document
            }
        }
    }

    fun setCameraTargetAndZoom(
        position: LatLng = LatLng(37.510402, 126.945915),
        zoom: Float = 17f
    ) {
        _cameraTarget.value = position
        _cameraZoom.value = zoom
    }

    fun changeFilterState(filterState : String = "all") {
        _filterState.value = filterState
    }
}

