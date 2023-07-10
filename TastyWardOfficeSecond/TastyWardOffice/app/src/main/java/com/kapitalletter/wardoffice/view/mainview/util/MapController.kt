package com.kapitalletter.wardoffice.view.mainview.util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class MapController(
    private val googleMap: GoogleMap
    ) {

    fun mapLimitBoundaryKorea() {
        val builder = LatLngBounds.Builder()
        builder.include(LatLng(33.1422, 124.0384))
        builder.include(LatLng(38.6120, 131.2361))
        val boundary = builder.build()
        googleMap.setLatLngBoundsForCameraTarget(boundary)
    }

    fun moveUserViewLocation(target : LatLng, zoom : Float) {
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                target,
                zoom
            )
        )
    }


}