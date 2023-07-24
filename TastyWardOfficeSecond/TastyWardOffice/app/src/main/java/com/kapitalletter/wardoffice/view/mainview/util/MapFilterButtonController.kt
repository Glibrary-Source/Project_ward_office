package com.kapitalletter.wardoffice.view.mainview.util

import android.content.Context
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.data.WardOfficeGeo
import com.kapitalletter.wardoffice.databinding.FragmentGoogleMapFilterButtonBinding
import com.kapitalletter.wardoffice.databinding.FragmentGoogleMapMenuButtonBinding
import com.kapitalletter.wardoffice.view.mainview.viewmodel.OverviewViewModel

class MapFilterButtonController(
    private val googleMap: GoogleMap,
    private val overViewModel: OverviewViewModel,
    private val menuView: FragmentGoogleMapMenuButtonBinding,
    private val localView: FragmentGoogleMapFilterButtonBinding,
    private val mapController: MapController,
    private val context: Context
    ) {

    private val locationGeoData: WardOfficeGeo = WardOfficeGeo()

    fun setFilterOnClickButton(p0: View?) {
        when (p0?.id) {
            R.id.filter_button_all -> {
                filterMenuButtonAction(context.getString(R.string.all), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_korean -> {
                filterMenuButtonAction(context.getString(R.string.korean), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_china -> {
                filterMenuButtonAction(context.getString(R.string.chines), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_japan -> {
                filterMenuButtonAction(context.getString(R.string.japan), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_kimbap -> {
                filterMenuButtonAction(context.getString(R.string.kimbap), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_western -> {
                filterMenuButtonAction(context.getString(R.string.western), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_dessert -> {
                filterMenuButtonAction(context.getString(R.string.dessert), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_asian -> {
                filterMenuButtonAction(context.getString(R.string.asian), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_chiken -> {
                filterMenuButtonAction(context.getString(R.string.chiken), menuView)
                createMarkerAroundShop()
            }
            R.id.filter_button_bar -> {
                filterMenuButtonAction(context.getString(R.string.bar), menuView)
                createMarkerAroundShop()
            }

            R.id.title_button -> {
                titleButtonExpandAdjust()
            }
            R.id.filter_ward_office_button -> {
                filterButtonExpandAdjust()
            }

            R.id.filter_button_gangnam -> {
                setLocalButtonAction(
                    context.getString(R.string.gangnam)
                )
            }
            R.id.filter_button_gandong -> {
                setLocalButtonAction(
                    context.getString(R.string.gangdong)
                )
            }
            R.id.filter_button_gangbuk -> {
                setLocalButtonAction(
                    context.getString(R.string.gangbuk)
                )
            }
            R.id.filter_button_gangseo -> {
                setLocalButtonAction(
                    context.getString(R.string.gangseo)
                )
            }
            R.id.filter_button_gwanak -> {
                setLocalButtonAction(
                    context.getString(R.string.gwanak)
                )
            }
            R.id.filter_button_gwangjin -> {
                setLocalButtonAction(
                    context.getString(R.string.gwangjin)
                )
            }
            R.id.filter_button_guro -> {
                setLocalButtonAction(
                    context.getString(R.string.guro)
                )
            }
            R.id.filter_button_geumcheon -> {
                setLocalButtonAction(
                    context.getString(R.string.geumcheon)
                )
            }
            R.id.filter_button_nowon -> {
                setLocalButtonAction(
                    context.getString(R.string.nowon)
                )
            }
            R.id.filter_button_dobong -> {
                setLocalButtonAction(
                    context.getString(R.string.dobong)
                )
            }
            R.id.filter_button_ddm -> {
                setLocalButtonAction(
                    context.getString(R.string.ddm)
                )
            }
            R.id.filter_button_dongjak -> {
                setLocalButtonAction(
                    context.getString(R.string.dongjak)
                )
            }
            R.id.filter_button_mapo -> {
                setLocalButtonAction(
                    context.getString(R.string.mapo)
                )
            }
            R.id.filter_button_sdm -> {
                setLocalButtonAction(
                    context.getString(R.string.sdm)
                )
            }
            R.id.filter_button_seocho -> {
                setLocalButtonAction(
                    context.getString(R.string.seocho)
                )
            }
            R.id.filter_button_sd -> {
                setLocalButtonAction(
                    context.getString(R.string.sd)
                )
            }
            R.id.filter_button_sb -> {
                setLocalButtonAction(
                    context.getString(R.string.sb)
                )
            }
            R.id.filter_button_songpa -> {
                setLocalButtonAction(
                    context.getString(R.string.songpa)
                )
            }
            R.id.filter_button_yangcheon -> {
                setLocalButtonAction(
                    context.getString(R.string.yangcheon)
                )
            }
            R.id.filter_button_ydp -> {
                setLocalButtonAction(
                    context.getString(R.string.ydp)
                )
            }
            R.id.filter_button_yongsan -> {
                setLocalButtonAction(
                    context.getString(R.string.yongsan)
                )
            }
            R.id.filter_button_ep -> {
                setLocalButtonAction(
                    context.getString(R.string.ep)
                )
            }
            R.id.filter_button_jongno -> {
                setLocalButtonAction(
                    context.getString(R.string.jongno)
                )
            }
            R.id.filter_button_junggu -> {
                setLocalButtonAction(
                    context.getString(R.string.junggu)
                )
            }
            R.id.filter_button_jungnang -> {
                setLocalButtonAction(
                    context.getString(R.string.jungnang)
                )
            }
            else -> {
                overViewModel.changeFilterState(context.getString(R.string.dessert))
                menuView.layoutExpand.visibility = View.GONE
                googleMap.clear()
                createMarkerAroundShop()
            }
        }
    }

    fun filterViewExpandClose() {
        localView.layoutExpand2.visibility = View.GONE
        localView.layoutExpand3.visibility = View.GONE
        menuView.layoutExpand.visibility = View.GONE
    }

    private fun createMarkerAroundShop() {
        try {
            for (i in overViewModel.distanceStoreData.value!!.Filterstore) {
                mapController.createFilterStateMarker( overViewModel.filterState.value!!, i )
            }
        } catch (e: Exception) {

        }
    }

    private fun filterMenuButtonAction(
        filterMenu: String,
        menuView: FragmentGoogleMapMenuButtonBinding
    ) {
        overViewModel.changeFilterState(filterMenu)
        menuView.layoutExpand.visibility = View.GONE
        googleMap.clear()
    }

    private fun titleButtonExpandAdjust() {
        if (menuView.layoutExpand.visibility == View.VISIBLE) {
            menuView.layoutExpand.visibility = View.GONE

            localView.layoutExpand2.visibility = View.GONE
            localView.layoutExpand3.visibility = View.GONE
            localView.total.visibility = View.GONE

            menuView.imgMore1.animate().duration = 200
        } else {
            menuView.layoutExpand.visibility = View.VISIBLE

            localView.layoutExpand2.visibility = View.GONE
            localView.layoutExpand3.visibility = View.GONE
            localView.total.visibility = View.GONE

            menuView.imgMore1.animate().duration = 200
        }
    }

    private fun filterButtonExpandAdjust() {
        if (
            localView.layoutExpand2.visibility == View.VISIBLE &&
            localView.layoutExpand3.visibility == View.VISIBLE &&
            localView.total.visibility == View.VISIBLE
        ) {
            localView.layoutExpand2.visibility = View.GONE
            localView.layoutExpand3.visibility = View.GONE
            localView.total.visibility = View.GONE

            menuView.layoutExpand.visibility = View.GONE

            menuView.imgMore1.animate().duration = 200
        } else {
            localView.layoutExpand2.visibility = View.VISIBLE
            localView.layoutExpand3.visibility = View.VISIBLE
            localView.total.visibility = View.VISIBLE


            menuView.layoutExpand.visibility = View.GONE

            menuView.imgMore1.animate().duration = 200
        }
    }

    private fun setLocalButtonAction(
        localName: String
    ) {
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                locationGeoData.getWardGeo(
                    localName
                ), 17f
            )
        )
        localView.layoutExpand2.visibility = View.GONE
        localView.layoutExpand3.visibility = View.GONE
    }

}