package com.example.tastywardoffice.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tastywardoffice.R
import com.example.tastywardoffice.bindImage
import com.example.tastywardoffice.datamodel.FinalStoreDataModel
import com.example.tastywardoffice.restaurant_listDirections
import com.google.android.gms.maps.model.LatLng

class StoreListAdapter(private val context: Context, private val dataset: FinalStoreDataModel
): RecyclerView.Adapter<StoreListAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView : TextView = view.findViewById(R.id.name_store)
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
        holder.textView.text = item.document.storeId
        bindImage(holder.menuImage, item.document.storeMenuPictureUrlsStore[0])
        holder.priceAverage.text = "${item.document.storePriceMin}원~${item.document.storePriceMax}원"
        holder.menuImage.clipToOutline = true
        holder.itemView.setOnClickListener {
            val action = restaurant_listDirections.actionRestaurantListToDetailMenu3(
                storename = holder.textView.text.toString(),
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