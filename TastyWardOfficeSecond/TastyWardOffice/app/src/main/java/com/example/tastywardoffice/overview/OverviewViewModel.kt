package com.example.tastywardoffice.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastywardoffice.network.TastyApi
import com.example.tastywardoffice.network.TastyPhoto
import com.google.android.datatransport.runtime.util.PriorityMapping.toInt
import kotlinx.coroutines.launch

enum class TastyApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<TastyApiStatus>()
    private val _photos = MutableLiveData<List<TastyPhoto>>()

    // The external immutable LiveData for the request status
    val status: LiveData<TastyApiStatus> = _status
    val photos: LiveData<List<TastyPhoto>> = _photos

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [LiveData].
     */
    private fun getMarsPhotos() {
        viewModelScope.launch {
            _status.value = TastyApiStatus.LOADING
            try {
                _photos.value = TastyApi.retrofitService.getPhotos()

                //데이터 개수 또는 조건문을 통한 데이터 개수 제한
//                val testMutableList = mutableListOf<TastyPhoto>()
//                for(value in _photos.value!!){
//                    if (value.id.toInt() <= 424909) {
//                        testMutableList.add(value)
//                    }
//                }
//                _photos.value = testMutableList

                _status.value = TastyApiStatus.DONE
            } catch (e: Exception) {
                _status.value = TastyApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }

}