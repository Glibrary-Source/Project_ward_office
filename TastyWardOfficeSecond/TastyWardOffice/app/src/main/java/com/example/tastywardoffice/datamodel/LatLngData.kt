package com.example.tastywardoffice.datamodel

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class LatLngData(
    val index : Int,
    val id : Long,
    val latlng: LatLng) : ClusterItem {
    override fun getPosition(): LatLng {
        return latlng
    }
    override fun getTitle(): String? {
        TODO("Not yet implemented")
    }

    override fun getSnippet(): String? {
        TODO("Not yet implemented")
    }
}