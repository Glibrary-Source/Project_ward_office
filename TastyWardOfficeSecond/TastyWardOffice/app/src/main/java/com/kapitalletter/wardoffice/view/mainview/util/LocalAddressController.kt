package com.kapitalletter.wardoffice.view.mainview.util

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.*

class LocalAddressController(
    private val context: Context,
) {

    fun getDetailStoreAddress(
        storeDataLatLng: LatLng
    ): String {
        val geocoder = Geocoder(context, Locale.KOREA)
        return geocoder.getFromLocation(storeDataLatLng.latitude, storeDataLatLng.longitude, 1)[0]
            .getAddressLine(0)!!.substring(5)
    }

}