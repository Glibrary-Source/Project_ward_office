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
        bindImage(holder.detailMenu, item)
        holder.detailMenu.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return dataset.storeMenuPictureUrlsMenu.size
    }

}