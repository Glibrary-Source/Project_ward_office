package com.kapitalletter.wardoffice.view.mainview.adapter

import android.annotation.SuppressLint
import android.app.Activity
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
import com.kapitalletter.wardoffice.MyGlobals
import com.kapitalletter.wardoffice.view.mainview.FragmentRestaurantListDirections
import com.kapitalletter.wardoffice.view.mainview.util.StoreListAdapterItemRank

class StoreListAdapter(
    private val dataset: FinalStoreDataModel,
    private val context: Context
): RecyclerView.Adapter<StoreListAdapter.ItemViewHolder>() {

    private var storeListAdapterItemRank = StoreListAdapterItemRank()

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
        val item = dataset.filterStore[position]

        holder.storeTextView.text = item.document.storeId

        try{ bindImage(holder.menuImage, item.document.storeMenuPictureUrlsStore[0]) }
        catch (e: Exception) { holder.menuImage.setImageResource(R.drawable.blank_img) }

        storeListAdapterItemRank.setRankText(item, holder)

        holder.priceAverage.text = "${item.document.storePriceMin}원~${item.document.storePriceMax}원"
        holder.menuImage.clipToOutline = true
        holder.itemView.setOnClickListener {
            val action = FragmentRestaurantListDirections.actionRestaurantListToDetailMenu3(
                storename = holder.storeTextView.text.toString(),
                dogId = item.document.docId,
                latlng = LatLng(item.document.storeGEOPoints[0], item.document.storeGEOPoints[1])
            )
            holder.itemView.findNavController().navigate(action)

            try{
                if (MyGlobals.instance?.adMobCount!! % 5 == 0) {
                    MyGlobals.instance?.fullAD!!.show(context as Activity)
                }
            } catch (e:Exception) {

            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.filterStore.size
    }
}