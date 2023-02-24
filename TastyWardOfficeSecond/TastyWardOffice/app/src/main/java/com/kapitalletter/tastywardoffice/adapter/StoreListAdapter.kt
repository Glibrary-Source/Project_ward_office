package com.kapitalletter.tastywardoffice.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kapitalletter.tastywardoffice.R
import com.kapitalletter.tastywardoffice.bindImage
import com.kapitalletter.tastywardoffice.datamodel.FinalStoreDataModel
import com.kapitalletter.tastywardoffice.restaurant_listDirections
import com.google.android.gms.maps.model.LatLng

class StoreListAdapter(
    private val dataset: FinalStoreDataModel
): RecyclerView.Adapter<StoreListAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeTextView : TextView = view.findViewById(R.id.name_store)
        val menuImage : ImageView = view.findViewById(R.id.menu_image)
        val priceAverage : TextView = view.findViewById(R.id.price_average)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_view_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset.Filterstore[position]
        holder.storeTextView.text = item.document.storeId
        bindImage(holder.menuImage, item.document.storeMenuPictureUrlsStore[0])
        holder.priceAverage.text = "${item.document.storePriceMin}원~${item.document.storePriceMax}원"
        holder.menuImage.clipToOutline = true
        holder.itemView.setOnClickListener {
            val action = restaurant_listDirections.actionRestaurantListToDetailMenu3(
                storename = holder.storeTextView.text.toString(),
                dogId = item.document.docId,
                latlng = LatLng(item.document.storeGEOPoints[0], item.document.storeGEOPoints[1])
            )
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataset.Filterstore.size
    }

}