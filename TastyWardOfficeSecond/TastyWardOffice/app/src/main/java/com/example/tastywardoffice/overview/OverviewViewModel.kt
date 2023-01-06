package com.example.tastywardoffice.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tastywardoffice.datamodel.DistanceToData
import com.example.tastywardoffice.datamodel.RequestLocationData
import com.example.tastywardoffice.network.*
import com.google.android.gms.maps.model.LatLng
import retrofit2.Callback
import retrofit2.Response

enum class TastyApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    private val _distanceStoreData = MutableLiveData<DistanceToData>()
    private val _cameraTarget = MutableLiveData<LatLng>()

    val distanceStoreData: LiveData<DistanceToData> = _distanceStoreData
    val cameraTarget: LiveData<LatLng> = _cameraTarget

    init {
        saveCameraTarget()
    }

    fun distanceTo(position: LatLng = LatLng(37.510402, 126.945915)) {
        val myLocation = listOf<Double>(position.latitude, position.longitude)
        val requestType = RequestLocationData("How_long", myLocation)
        TastyWardApi.service.getLocationDistanceTo(requestType).enqueue(object : Callback<DistanceToData> {
            override fun onResponse(call: retrofit2.Call<DistanceToData>, response: Response<DistanceToData>) {
                if (response.isSuccessful) {
                    Log.d("LocationDB", position.toString())
                    _distanceStoreData.value = response.body()
                } else {
                    val result: DistanceToData? = response.body()
                    Log.d("LocationDB", "onResponse 실패 " + result?.toString())
                }
            }
            override fun onFailure(call: retrofit2.Call<DistanceToData>, t: Throwable) {
                Log.d("LocationDB", "onFailure 에러 " + t.message.toString())
            }
        })
    }

    fun saveCameraTarget(position: LatLng = LatLng(37.510402, 126.945915)) {
        _cameraTarget.value = position
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
