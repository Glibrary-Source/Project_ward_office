package com.kapitalletter.wardoffice

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.maps.model.LatLng


class MyGlobals {

    var detailLatLng: LatLng? = null
    var detailStoreId: String? = null
    var adMobCount: Int = 1
    var fullAD: InterstitialAd? = null

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
