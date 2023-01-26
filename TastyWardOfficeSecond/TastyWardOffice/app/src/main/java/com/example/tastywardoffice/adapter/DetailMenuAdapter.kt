package com.example.tastywardoffice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tastywardoffice.R
import com.example.tastywardoffice.bindImage
import com.example.tastywardoffice.datamodel.Documents

class DetailMenuAdapter(private val context: Context, private val dataset: Documents
): RecyclerView.Adapter<DetailMenuAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
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
    }

    override fun getItemCount(): Int {
        return dataset.storeMenuPictureUrlsMenu.size
    }

}