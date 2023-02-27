package com.kapitalletter.tastywardoffice

import com.google.android.gms.maps.model.LatLng


class MyGlobals {

    //디테일 메뉴 지도를 띄우기위한 전역 변수들
    var detailLatLng: LatLng? = null
    var detailStoreId: String? = null

    companion object {
        @get:Synchronized
        var instance: MyGlobals? = null
            get() {
                if (null == field) {
                    field = MyGlobals()
                }
                return field
            }
            private set
    }
}