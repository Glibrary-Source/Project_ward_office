package com.kapitalletter.wardoffice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.bindImage
import com.kapitalletter.wardoffice.datamodel.Documents

class DetailMenuAdapter(
    private val dataset: Documents
): RecyclerView.Adapter<DetailMenuAdapter.ItemViewHolder>() {

    private var isEnlarged = false

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val detailMenu : ImageView = view.findViewById(R.id.detail_menu_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_menu_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset.storeMenuPictureUrlsMenu[position]
        try{
            bindImage(holder.detailMenu, item)
            holder.detailMenu.setOnClickListener {
                if(isEnlarged) {
                    it.scaleX = 1f
                    it.scaleY = 1f
                    isEnlarged = false
                } else {
                    it.scaleX = 1.5f
                    it.scaleY = 1.5f
                    isEnlarged = true
                }
            }
        }
        catch (e:Exception) {holder.detailMenu.setImageResource(R.drawable.blank_img)}
    }

    override fun getItemCount(): Int {
        return dataset.storeMenuPictureUrlsMenu.size
    }

}