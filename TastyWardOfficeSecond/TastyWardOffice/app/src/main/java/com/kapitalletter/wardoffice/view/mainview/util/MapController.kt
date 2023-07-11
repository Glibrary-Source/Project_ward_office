package com.kapitalletter.wardoffice.view.mainview.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.navigation.NavDirections
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.datamodel.FilterStore
import com.kapitalletter.wardoffice.view.FragmentGoogleMapDirections

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

    fun moveUserViewLocation(target: LatLng, zoom: Float) {
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                target,
                zoom
            )
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun addMarkCurrentLocation(permissionModule: CheckPermission) {
        val currentDrawable =
            context.resources.getDrawable(
                R.drawable.marker_icons_mylocation,
                null
            ) as BitmapDrawable
        val img = currentDrawable.bitmap
        val currentLocationMarker = Bitmap.createScaledBitmap(img, 80, 80, false)

        googleMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(currentLocationMarker))
                .position(LatLng(permissionModule.latitude, permissionModule.longitude))
                .title("현위치")
        )
    }

    fun createFilterStateMarker(
        filterState: String,
        filterStore: FilterStore,
    ) {
        when (filterState) {
            context.getString(R.string.korean) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.korean))
            }
            context.getString(R.string.japan) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.japan))
            }
            context.getString(R.string.chines) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.chines))
            }
            context.getString(R.string.kimbap) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.kimbap))
            }
            context.getString(R.string.western) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.western))
            }
            context.getString(R.string.dessert) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.dessert))
            }
            context.getString(R.string.asian) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.asian))
            }
            context.getString(R.string.chiken) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.chiken))
            }
            context.getString(R.string.bar) -> {
                choiceStoreMarker( filterStore, context.getString(R.string.bar))
            }
            else -> {
                setMarker(filterStore)
            }
        }
    }

    private fun choiceStoreMarker(
        filterStore: FilterStore,
        storeMenu: String
    ) {
        if (filterStore.document.storeTitle == storeMenu) {
            setMarker(filterStore)
        }
    }

    private fun setMarker(filterStore: FilterStore) {
        googleMap.addMarker(
            MarkerOptions()
                .position(setStoreLatLng(filterStore))
                .title(filterStore.document.storeId)
                .icon(BitmapDescriptorFactory.fromBitmap(createMarker(filterStore)))
        )
    }

    private fun setStoreLatLng(filterStore: FilterStore) : LatLng {
        return LatLng(filterStore.document.storeGEOPoints[0], filterStore.document.storeGEOPoints[1])
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createMarker(filterStore: FilterStore): Bitmap {
        val storeDrawable =
            when (filterStore.document.storeTitle) {
                context.getString(R.string.japan) -> R.drawable.marker_icons_food_sushi
                context.getString(R.string.chines) -> R.drawable.marker_icons_food_china
                context.getString(R.string.korean) -> R.drawable.marker_icons_food_korean
                context.getString(R.string.kimbap) -> R.drawable.marker_icons_food_kimbap
                context.getString(R.string.western) -> R.drawable.marker_icons_food_western
                context.getString(R.string.dessert) -> R.drawable.marker_icons_food_coffee
                context.getString(R.string.asian) -> R.drawable.marker_icons_food_asian
                context.getString(R.string.chiken) -> R.drawable.marker_icons_food_chiken
                context.getString(R.string.bar) -> R.drawable.marker_icons_food_bar
                else -> R.drawable.marker_icons_food_sushi
            }
        val bitMapDraw =
            context.resources.getDrawable(storeDrawable, null) as BitmapDrawable
        val b = bitMapDraw.bitmap
        return Bitmap.createScaledBitmap(b, 84, 84, false)
    }

    fun mapInfoClickAction(p0: Marker, docId: String): NavDirections {
        return FragmentGoogleMapDirections.actionGoogleMapToDetailMenu3(
            p0.title.toString(),
            docId,
            p0.position
        )
    }


}