package com.example.tastywardoffice.overview

import android.graphics.Paint
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.tastywardoffice.network.*

import retrofit2.Callback
import retrofit2.Response

class TestViewModel {
    var result2: String? = null
    init {
        getStores()
    }
    fun getStores() {
        val tempData = JoinData("filter_stores", "E", 5, 4000, 6000)
        TastyWardApi.service.getStoreData(tempData)?.enqueue(object : Callback<MyDTO> {
            override fun onResponse(call: retrofit2.Call<MyDTO>, response: Response<MyDTO>) {
                if (response.isSuccessful) {
                    var result = response.body()!!
                    result2 = result.secondData.storeId
                    Log.d("YMC", " onResponse 성공 " + result.toString())
                } else {
                    var result = response.body()!!
                    result2 = result.secondData.storeId
                    Log.d("YMC", "onResponse 실패 " + "이거아님")
                }
            }
            override fun onFailure(call: retrofit2.Call<MyDTO>, t: Throwable) {
                Log.d("YMC", "onFailure 에러 " + t.message.toString())
            }
        })
    }
}
