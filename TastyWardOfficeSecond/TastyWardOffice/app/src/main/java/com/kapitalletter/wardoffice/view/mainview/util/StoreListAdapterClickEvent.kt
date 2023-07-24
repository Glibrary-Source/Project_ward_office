package com.kapitalletter.wardoffice.view.mainview.util

import android.view.View
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import com.kapitalletter.wardoffice.datamodel.FilterStore
import com.kapitalletter.wardoffice.view.mainview.FragmentRestaurantListDirections
import com.kapitalletter.wardoffice.view.mainview.adapter.StoreListAdapter

class StoreListAdapterClickEvent {

    fun actionRestaurantListToDetail(
        holder: StoreListAdapter.ItemViewHolder,
        item: FilterStore,
        admobController: AdmobController
    ): View.OnClickListener {

        val clickListener = View.OnClickListener {
            val action = FragmentRestaurantListDirections.actionRestaurantListToDetailMenu3 (
                storename = holder.storeTextView.text.toString(),
                docId = item.document.docId,
                latlng = LatLng(item.document.storeGEOPoints[0], item.document.storeGEOPoints[1])
            )
            holder.itemView.findNavController().navigate(action)
            admobController.mapInfoClickCallAd()
        }

        return clickListener
    }
}