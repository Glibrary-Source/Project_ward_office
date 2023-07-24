package com.kapitalletter.wardoffice.view.mainview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.adapter.bindImage
import com.kapitalletter.wardoffice.datamodel.FinalStoreDataModel
import com.google.android.gms.maps.model.LatLng
import com.kapitalletter.wardoffice.view.mainview.FragmentRestaurantListDirections
import com.kapitalletter.wardoffice.view.mainview.util.AdmobController
import com.kapitalletter.wardoffice.view.mainview.util.StoreListAdapterClickEvent
import com.kapitalletter.wardoffice.view.mainview.util.StoreListAdapterItemRank

class StoreListAdapter(
    private val dataset: FinalStoreDataModel,
    private val context: Context
): RecyclerView.Adapter<StoreListAdapter.ItemViewHolder>() {

    private var storeListAdapterItemRank = StoreListAdapterItemRank()
    private var admobController = AdmobController(context)
    private var storeListAdapterClickEvent = StoreListAdapterClickEvent()

    init {
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val storeTextView : TextView = view.findViewById(R.id.name_store)
        val menuImage : ImageView = view.findViewById(R.id.menu_image)
        val priceAverage : TextView = view.findViewById(R.id.price_average)
        val rankText : TextView = view.findViewById(R.id.img_rank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_view_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset.Filterstore[position]

        try{ bindImage(holder.menuImage, item.document.storeMenuPictureUrls.store[0]) }
        catch (e: Exception) { holder.menuImage.setImageResource(R.drawable.blank_img) }

        holder.storeTextView.text = item.document.storeId
        holder.priceAverage.text = "${item.document.storePriceMin}원~${item.document.storePriceMax}원"
        holder.menuImage.clipToOutline = true

        holder.itemView.setOnClickListener(
            storeListAdapterClickEvent.actionRestaurantListToDetail(holder, item, admobController)
        )

        storeListAdapterItemRank.setRankText(item, holder)
    }

    override fun getItemCount(): Int {
        return dataset.Filterstore.size
    }

}