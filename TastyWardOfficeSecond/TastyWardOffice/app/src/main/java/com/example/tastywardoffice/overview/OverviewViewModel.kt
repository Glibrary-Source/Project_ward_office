package com.example.tastywardoffice.overview

import android.location.Geocoder
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastywardoffice.network.*
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.Result

enum class TastyApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<TastyApiStatus>()
    private val _photos = MutableLiveData<List<TastyPhoto>>()
//    private val _testDTO = MutableLiveData<MyDTO>()



    // The external immutable LiveData for the request status
    val status: LiveData<TastyApiStatus> = _status
    val photos: LiveData<List<TastyPhoto>> = _photos
//    val testDTO: LiveData<MyDTO> = _testDTO


    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
//        getStores()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [LiveData].
     */
//
//    private fun getStores() {
//        val tempData = JoinData("filter_stores","E",5,4000,6000)
//        viewModelScope.launch{
//            TastyWardApi.service.getStoreData(tempData)?.enqueue(object : Callback<MyDTO> {
//                override fun onResponse(call: retrofit2.Call<MyDTO>, response: Response<MyDTO>) {
//                    if (response.isSuccessful) {
//                        _testDTO.value = response.body()
//                        Log.d("YMC", " onResponse 성공 " + _testDTO.value.toString())
//                    } else {
//                        var result: MyDTO? = response.body()
//                        Log.d("YMC", "onResponse 실패 " + "이거아님")
//                    }
//                }
//                override fun onFailure(call: retrofit2.Call<MyDTO>, t: Throwable) {
//                    Log.d("YMC", "onFailure 에러 " + t.message.toString())
//                }
//            })
//        }
//    }

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
