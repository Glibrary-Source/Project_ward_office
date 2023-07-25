package com.kapitalletter.wardoffice.view.mainview.util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.kapitalletter.wardoffice.MyGlobals

class DetailGoogleMapMarker(
    private val googleMap: GoogleMap
) {

    private lateinit var current: LatLng

    init {
        getCurrentLatLng()
    }

    fun setDetailGoogleMap () {
        addCurrentStoreMarker()
        moveCurrentLatLng()
        mapLimitBoundaryCurrentStore()
    }

    private fun addCurrentStoreMarker() {
        googleMap.addMarker(
            MarkerOptions()
                .position(current)
                .title(MyGlobals.instance?.detailStoreId)
        )!!.showInfoWindow()
    }

    private fun moveCurrentLatLng() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16f))
    }

    private fun mapLimitBoundaryCurrentStore() {
        val builder = LatLngBounds.Builder()
        builder.include(current)
        builder.include(current)
        val bounds = builder.build()
        googleMap.setLatLngBoundsForCameraTarget(bounds)
    }

    private fun getCurrentLatLng() {
        current = MyGlobals.instance?.detailLatLng!!
    }

}