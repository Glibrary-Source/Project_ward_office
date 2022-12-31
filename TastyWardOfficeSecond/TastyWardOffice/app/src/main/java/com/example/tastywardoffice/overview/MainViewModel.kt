package com.example.tastywardoffice.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tastywardoffice.network.JoinData
import com.example.tastywardoffice.network.RequestType


class MainViewModel(): ViewModel() {
    private var _height = MutableLiveData<Int>()

    val height: LiveData<Int>
    get() = _height

    init {
        _height.value = 170
    }

    fun increase() {
        _height.value = _height.value?.plus(1)
    }
    //api 데이터를 뷰모델에 받아오기

    fun totalShopData() {
        val tempData = RequestType("whole_stores")

    }

}