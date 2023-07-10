package com.kapitalletter.wardoffice.view.mainview.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.datamodel.FilterStore

class MapController(
    private val googleMap: GoogleMap,
    private val context: Context
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

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setMarkCurrentLocation(permissionModule: CheckPermission) {
        val currentDrawable =
            context.resources.getDrawable(R.drawable.marker_icons_mylocation, null) as BitmapDrawable
        val img = currentDrawable.bitmap
        val currentLocationMarker = Bitmap.createScaledBitmap(img, 80, 80, false)

        googleMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(currentLocationMarker))
                .position(LatLng(permissionModule.latitude, permissionModule.longitude))
                .title("현위치")
        )
    }

    fun addStoreMarker(
        storeLatLng: LatLng,
        i: FilterStore,
        smallMarker: Bitmap
    ) {
        googleMap.addMarker(
            MarkerOptions()
                .position(storeLatLng)
                .title(i.document.storeId)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        )
    }


}