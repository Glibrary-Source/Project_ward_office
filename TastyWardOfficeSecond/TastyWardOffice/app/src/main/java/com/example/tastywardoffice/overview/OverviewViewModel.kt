package com.example.tastywardoffice.overview

import android.location.Geocoder
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastywardoffice.datamodel.DistanceToData
import com.example.tastywardoffice.datamodel.RequestLocationData
import com.example.tastywardoffice.network.*
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.Result

enum class TastyApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    private val _status = MutableLiveData<TastyApiStatus>()
    private val _photos = MutableLiveData<List<TastyPhoto>>()
    private val _distanceStoreData = MutableLiveData<DistanceToData>()

    val status: LiveData<TastyApiStatus> = _status
    val photos: LiveData<List<TastyPhoto>> = _photos
    val distanceStoreData: LiveData<DistanceToData> = _distanceStoreData

    init {
        getMarsPhotos()
        distanceTo(position = LatLng(37.510402, 126.945915))
    }

    private fun getMarsPhotos() {
        viewModelScope.launch {
            _status.value = TastyApiStatus.LOADING
            try {
                _photos.value = TastyApi.retrofitService.getPhotos()
                _status.value = TastyApiStatus.DONE
            } catch (e: Exception) {
                _status.value = TastyApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }

    fun distanceTo(position: LatLng) {
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
