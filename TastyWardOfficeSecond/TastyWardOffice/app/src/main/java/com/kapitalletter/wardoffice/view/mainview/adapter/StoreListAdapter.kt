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
import com.kapitalletter.wardoffice.restaurant_listDirections
import com.google.android.gms.maps.model.LatLng
import com.kapitalletter.wardoffice.MyGlobals

class StoreListAdapter(
    private val dataset: FinalStoreDataModel,
    private val context: Context
): RecyclerView.Adapter<StoreListAdapter.ItemViewHolder>() {


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

        try{
            bindImage(holder.menuImage, item.document.storeMenuPictureUrlsStore[0])
        }
        catch (e: Exception) {holder.menuImage.setImageResource(R.drawable.blank_img)}

        //좋아요 수에따른 구분
        if(item.document.storeCntLikes >= 300) {
            holder.rankText.text = "1급"
        }
        else if(item.document.storeCntLikes >= 250){
            holder.rankText.text = "2급"
        }
        else if(item.document.storeCntLikes >= 200){
            holder.rankText.text = "3급"
        }
        else if(item.document.storeCntLikes >= 150 ){
            holder.rankText.text = "4급"
        }
        else if(item.document.storeCntLikes >= 100){
            holder.rankText.text = "5급"
        }
        else if(item.document.storeCntLikes >= 50){
            holder.rankText.text = "6급"
        }
        else if(item.document.storeCntLikes >= 40){
            holder.rankText.text = "7급"
        }
        else if(item.document.storeCntLikes >= 10){
            holder.rankText.text = "8급"
        }
        else{
            holder.rankText.text = "9급"
        }

        holder.priceAverage.text = "${item.document.storePriceMin}원~${item.document.storePriceMax}원"
        holder.menuImage.clipToOutline = true
        holder.itemView.setOnClickListener {
            val action = restaurant_listDirections.actionRestaurantListToDetailMenu3(
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