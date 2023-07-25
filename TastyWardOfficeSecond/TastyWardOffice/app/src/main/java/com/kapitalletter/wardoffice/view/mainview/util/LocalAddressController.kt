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
        val multipleStoreLocation =
            geocoder.getFromLocation(storeDataLatLng.latitude, storeDataLatLng.longitude, 3)!!

        val storeLocationList = mutableListOf<String>()
        for (address in multipleStoreLocation) {
            storeLocationList.add(address.getAddressLine(0))
        }

        val splitStore = storeLocationList[0].split(" ").toSet().toMutableList()

        if (splitStore.contains("KR")) {
            splitStore.remove("KR")
        }
        if (splitStore.contains("대한민국")) {
            splitStore.remove("대한민국")
        }

        return splitStore.joinToString(" ")
    }

}