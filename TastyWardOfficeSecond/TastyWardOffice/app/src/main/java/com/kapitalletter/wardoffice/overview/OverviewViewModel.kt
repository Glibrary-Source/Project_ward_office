package com.kapitalletter.wardoffice.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.kapitalletter.wardoffice.datamodel.*
import com.kapitalletter.wardoffice.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverviewViewModel : ViewModel() {

    private val _distanceStoreData = MutableLiveData<FinalStoreDataModel>()
    private val _cameraTarget = MutableLiveData<LatLng>()
    private val _markerStoreData = MutableLiveData<Documents>()
    private val _cameraZoom = MutableLiveData<Float>()
    private val _filterState = MutableLiveData<String>()
    private val _reviewData = MutableLiveData<ReviewData>()

    val distanceStoreData: LiveData<FinalStoreDataModel> get() = _distanceStoreData
    val cameraTarget: LiveData<LatLng> get() = _cameraTarget
    val markerStoreData: LiveData<Documents> get() = _markerStoreData
    val cameraZoom: LiveData<Float> get() = _cameraZoom
    val filterState: LiveData<String> get() = _filterState
    val reviewData: LiveData<ReviewData> get() = _reviewData

    init {
        saveCameraTarget()
        cameraZoomState()
        changeFilterState()
    }

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

    fun saveCameraTarget(position: LatLng = LatLng(37.510402, 126.945915)) {
        _cameraTarget.value = position
    }

    fun cameraZoomState(zoom: Float = 17f) {
        _cameraZoom.value = zoom
    }

    fun changeFilterState(filterState : String = "all") {
        _filterState.value = filterState
    }

    //리뷰 작성
    fun createReview(
        store_id: String,
        reviewContext:String,
        reviewerNickname: String,
        reviewerUid: String ) {

        val createReview = CreateReview(
            "store_review",
            store_id,
            reviewContext,
            reviewerNickname,
            reviewerUid
        )
        TastyWardApi.service.createReview(createReview)
            .enqueue(object : Callback<CreateReview> {
            override fun onResponse(call: Call<CreateReview>, response: Response<CreateReview>) {
                if (response.isSuccessful) {
                    Log.d("testPost", response.body().toString())
                }
            }
            override fun onFailure(call: Call<CreateReview>, t: Throwable) {
                Log.d("testPost", t.toString())
            }
        })
    }

    fun readReview(docId: String) {
        val request = ReadReview("read_review", docId)
        TastyWardApi.service.readReview(request)
            .enqueue(object : Callback<ReviewData> {
                override fun onResponse(call: Call<ReviewData>, response: Response<ReviewData>) {
                    if (response.isSuccessful) {
                        _reviewData.value = response.body()
                    }
                }
                override fun onFailure(call: Call<ReviewData>, t: Throwable) {
                    Log.d("testPost", t.toString())
                }
            })
    }
}

